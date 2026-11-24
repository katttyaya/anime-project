package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    val favorites: StateFlow<List<Anime>> = repository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch { repository.toggleFavorite(anime) }
    }
}