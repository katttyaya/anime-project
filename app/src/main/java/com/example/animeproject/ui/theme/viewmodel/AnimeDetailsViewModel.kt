package com.example.animeproject.ui.theme.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.animeproject.data.Anime
import com.example.animeproject.data.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnimeDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: String = savedStateHandle.get<String>("animeId") ?: ""

    private val _anime = MutableStateFlow<Anime?>(null)
    val anime: StateFlow<Anime?> = _anime

    init {
        _anime.value = MockData.animeList.find { it.id == animeId }
    }
}