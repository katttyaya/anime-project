package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.data.local.SettingsDataStore
import com.example.animeproject.dependencies.FilterCache
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository
import com.example.animeproject.util.safeApiCall
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AnimeListViewModel(
    private val repository: AnimeRepository,
    private val settingsDataStore: SettingsDataStore,
    private val filterCache: FilterCache
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState.asStateFlow()

    val favorites: StateFlow<List<Anime>> = repository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            combine(
                settingsDataStore.searchQueryFlow,
                settingsDataStore.minRatingFlow,
                settingsDataStore.statusFilterFlow
            ) { query, rating, status -> Triple(query, rating, status) }
                .collect { (query, rating, status) ->
                    filterCache.updateFiltersState(query, rating, status)
                    loadAnimeList(query, rating, status)
                }
        }
    }

    private suspend fun loadAnimeList(query: String, minRating: Float, status: String) {
        _uiState.value = UiState.Loading
        val result = safeApiCall { repository.getAnimeList(query) }
        result.onSuccess { list ->
            val filtered = list.filter { anime ->
                (status == "Any" || anime.status.equals(status, ignoreCase = true)) &&
                        (anime.rating.toFloatOrNull() ?: 0f) >= minRating
            }
            _uiState.value = UiState.Success(filtered)
        }.onFailure {
            val favs = favorites.value
            _uiState.value = if (favs.isNotEmpty()) UiState.Success(favs)
            else UiState.Error("Нет сети. Показываю избранное.")
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch { repository.toggleFavorite(anime) }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}