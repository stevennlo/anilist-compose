package com.example.anilist.ui.view.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilist.model.Result
import com.example.anilist.repository.MediaRepository
import com.example.anilist.util.CalendarUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(private val mediaRepository: MediaRepository) :
    ViewModel() {

    var currentSeason = CalendarUtil.getCurrentSeason()
        private set
    var nextSeason = CalendarUtil.getNextSeason()
        private set
    private val _uiState = MutableStateFlow(LandingActivity.UiState())
    val uiState: StateFlow<LandingActivity.UiState> = _uiState

    init {
        getLandingMedia()
    }

    private fun refreshSeason() {
        currentSeason = CalendarUtil.getCurrentSeason()
        nextSeason = CalendarUtil.getNextSeason()
    }

    private fun getLandingMedia() {
        refreshSeason()
        viewModelScope.launch {
            mediaRepository.getLandingMedia(
                currentSeason.second,
                currentSeason.first,
                nextSeason.second,
                nextSeason.first
            ).collect { result ->
                when (result) {
                    is Result.Success -> _uiState.update {
                        it.copy(
                            landingMedia = result.data,
                            initLoading = false,
                            refreshing = false
                        )
                    }
                    is Result.Error -> _uiState.update {
                        it.copy(
                            error = result,
                            initLoading = false,
                            refreshing = false
                        )
                    }
                }
            }
        }
    }

    private fun getFavoriteMedia() {
        viewModelScope.launch {
            mediaRepository.getFavoritePopularMedia(6).collect { medias ->
                _uiState.update { it.copy(favoriteMedia = medias) }
            }
        }
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            UiEvent.Refresh -> {
                _uiState.update {
                    it.copy(refreshing = true)
                }
                getLandingMedia()
            }
            UiEvent.ErrorShown -> resetError()
            UiEvent.RefreshFavorites -> getFavoriteMedia()
        }
    }

    private fun resetError() {
        _uiState.update { it.copy(error = null) }
    }

    sealed class UiEvent {
        object RefreshFavorites : UiEvent()
        object Refresh : UiEvent()
        object ErrorShown : UiEvent()
    }
}