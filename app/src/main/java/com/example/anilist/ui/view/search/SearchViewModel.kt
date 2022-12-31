package com.example.anilist.ui.view.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anilist.model.MediaSeason
import com.example.anilist.model.MediaSort
import com.example.anilist.model.Result
import com.example.anilist.repository.MediaRepository
import com.example.anilist.util.AppUtil.get
import com.example.anilist.util.CalendarUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchActivity.UiState>
    val uiState: StateFlow<SearchActivity.UiState> get() = _uiState

    private var sort: MediaSort? = savedStateHandle[SearchActivity.EXTRA_SORT]
    private var season: MediaSeason? = savedStateHandle[SearchActivity.EXTRA_SEASON]
    private var seasonYear: Int? = savedStateHandle[SearchActivity.EXTRA_SEASON_YEAR]
    private var onlyFavorites: Boolean =
        savedStateHandle.get(SearchActivity.EXTRA_ONLY_FAVORITES, false)
    private var keyword: String? = null
    private val filters = mutableListOf<Any>()
    private var job: Job? = null
    private var filterParam: FilterScreenParam = getCurrentFilterParam()

    init {
        val initialSearch = savedStateHandle.get(SearchActivity.EXTRA_INITIAL_SEARCH, true)
        refreshFilters()
        _uiState = MutableStateFlow(
            SearchActivity.UiState(
                initialSearch = initialSearch,
                loading = !initialSearch,
                filters = getFilterLabels()
            )
        )
        refreshFilterParam()
        if (!initialSearch) {
            search()
        }
    }

    private fun refreshFilterParam() {
        filterParam = getCurrentFilterParam()
        _uiState.update { it.copy(filterScreenParam = filterParam) }
    }

    private fun getFilterLabels(): List<String> {
        return filters.map {
            when (it) {
                is MediaSeason -> it.label
                is MediaSort -> it.label
                is Boolean -> "♥"
                else -> it.toString()
            }
        }
    }

    private fun getCurrentFilterParam(): FilterScreenParam {
        val nextSeason = CalendarUtil.getNextSeason()
        return FilterScreenParam(
            year = nextSeason.first downTo 1990,
            selectedSeason = season,
            selectedYear = seasonYear,
            selectedSort = sort,
            onlyFavorites = onlyFavorites
        )
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            UiEvent.Refresh -> {
                _uiState.update {
                    it.copy(refreshing = true)
                }
                search()
            }
            UiEvent.ErrorShown -> resetError()
            is UiEvent.RemoveFilter -> {
                when (filters[event.position]) {
                    is MediaSeason -> season = null
                    is MediaSort -> sort = null
                    is Int -> seasonYear = null
                    is String -> keyword = null
                    is Boolean -> onlyFavorites = false
                }
                filters.removeAt(event.position)
                _uiState.update {
                    it.copy(
                        loading = true,
                        filters = getFilterLabels(),
                        medias = emptyList()
                    )
                }
                search()
            }
            is UiEvent.Search -> {
                val oldKeyword = keyword
                keyword = event.keyword.ifBlank { null }
                replaceFilter(keyword) { it is String }
                _uiState.update {
                    it.copy(
                        loading = true,
                        filters = getFilterLabels(),
                        medias = emptyList(),
                        initialSearch = false
                    )
                }
                if (oldKeyword != keyword) {
                    search()
                }
            }
            UiEvent.PersistFilter -> {
                if (filterParam.selectedSeason != season ||
                    filterParam.selectedYear != seasonYear ||
                    filterParam.selectedSort != sort ||
                    filterParam.onlyFavorites != onlyFavorites
                ) {
                    seasonYear = filterParam.selectedYear
                    season = filterParam.selectedSeason
                    sort = filterParam.selectedSort
                    onlyFavorites = filterParam.onlyFavorites
                    refreshFilters()
                    _uiState.update {
                        it.copy(
                            loading = true,
                            filters = getFilterLabels(),
                            medias = emptyList()
                        )
                    }
                    search()
                }
            }
            UiEvent.RefreshFilterParam -> refreshFilterParam()
            is UiEvent.UpdateOnlyFavoritesFilterParam -> {
                filterParam = filterParam.copy(onlyFavorites = event.onlyFavorites)
                updateFilterParam()
            }
            is UiEvent.UpdateSeasonFilterParam -> {
                filterParam = filterParam.copy(selectedSeason = event.season)
                updateFilterParam()
            }
            is UiEvent.UpdateSortFilterParam -> {
                filterParam = filterParam.copy(selectedSort = event.sort)
                updateFilterParam()
            }
            is UiEvent.UpdateYearFilterParam -> {
                filterParam = filterParam.copy(selectedYear = event.year)
                updateFilterParam()
            }
            UiEvent.RefreshFavorite -> if (onlyFavorites) {
                search()
            }
        }
    }

    private fun updateFilterParam() {
        _uiState.update {
            it.copy(
                filterScreenParam = filterParam
            )
        }
    }

    private fun refreshFilters() {
        replaceFilter(seasonYear) { it is Int }
        replaceFilter(season) { it is MediaSeason }
        replaceFilter(sort) { it is MediaSort }
        replaceFilter(onlyFavorites.takeIf { it }) { it is Boolean }
    }

    private fun replaceFilter(filter: Any?, condition: (Any) -> Boolean) {
        val index = filters.indexOfFirst(condition)
        if (index != -1) {
            filters.removeAt(index)
        }
        filter?.let { filters.add(it) }
    }

    private fun search() {
        job?.cancel()
        job = viewModelScope.launch {
            mediaRepository.searchMedia(keyword, season, seasonYear, sort, onlyFavorites)
                .collect { result ->
                    when (result) {
                        is Result.Success -> _uiState.update {
                            it.copy(
                                medias = result.data,
                                loading = false,
                                refreshing = false
                            )
                        }
                        is Result.Error -> _uiState.update {
                            it.copy(
                                error = result,
                                loading = false,
                                refreshing = false
                            )
                        }
                    }
                }
        }
    }

    private fun resetError() {
        _uiState.update { it.copy(error = null) }
    }

    sealed class UiEvent {
        object Refresh : UiEvent()
        object ErrorShown : UiEvent()
        data class RemoveFilter(val position: Int) : UiEvent()
        data class Search(val keyword: String) : UiEvent()
        data class UpdateSeasonFilterParam(val season: MediaSeason?) : UiEvent()
        data class UpdateYearFilterParam(val year: Int?) : UiEvent()
        data class UpdateSortFilterParam(val sort: MediaSort?) : UiEvent()
        data class UpdateOnlyFavoritesFilterParam(val onlyFavorites: Boolean) : UiEvent()
        object PersistFilter : UiEvent()
        object RefreshFilterParam : UiEvent()
        object RefreshFavorite : UiEvent()
    }
}