package com.example.anilist.ui.view.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilist.model.Media
import com.example.anilist.model.Result
import com.example.anilist.repository.MediaRepository
import com.example.anilist.util.AppDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val dispatcher: AppDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val media: Media? = savedStateHandle[DetailActivity.EXTRA_MEDIA]
    private val _uiState = MutableStateFlow(DetailActivity.UiState(media = media))
    val uiState: StateFlow<DetailActivity.UiState> = _uiState

    init {
        media?.let {
            checkFavorite()
            getCharacterAndRecommendation(it.id)
        }
    }

    private fun checkFavorite() {
        media?.let {
            viewModelScope.launch {
                val isFavorite = withContext(dispatcher.io()) {
                    mediaRepository.isFavorite(media.id)
                }
                _uiState.update {
                    it.copy(isFavorite = isFavorite)
                }
            }
        }
    }

    private fun getCharacterAndRecommendation(id: Int) {
        viewModelScope.launch {
            mediaRepository.getCharacterAndRecommendation(id).collect { result ->
                when (result) {
                    is Result.Success -> _uiState.update {
                        it.copy(
                            recommendations = result.data.second,
                            characters = result.data.first,
                            initLoading = false
                        )
                    }
                    is Result.Error -> _uiState.update {
                        it.copy(
                            initLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Favorite -> setFavorite(event.isFavorite)
            UiEvent.RefreshFavorites -> checkFavorite()
        }
    }

    private fun setFavorite(isFavorite: Boolean) {
        media?.let {
            viewModelScope.launch {
                withContext(dispatcher.io()) { mediaRepository.addFavorite(it, isFavorite) }
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    sealed class UiEvent {
        object RefreshFavorites : UiEvent()
        data class Favorite(val isFavorite: Boolean) : UiEvent()
    }
}