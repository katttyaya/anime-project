package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository
import com.example.animeproject.util.safeApiCall
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val repository: AnimeRepository,
    private val animeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val uiState: StateFlow<UiState<Anime>> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = repository.isFavoriteFlow(animeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = safeApiCall { repository.getAnimeById(animeId) }
            result.onSuccess { anime ->
                if (anime != null) _uiState.value = UiState.Success(anime)
                else _uiState.value = UiState.Error("Аниме не найдено")
            }.onFailure {
                val fav = repository.getFavoriteById(animeId)
                _uiState.value = if (fav != null) UiState.Success(fav)
                else UiState.Error("Нет сети")
            }
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch { repository.toggleFavorite(anime) }
    }
}