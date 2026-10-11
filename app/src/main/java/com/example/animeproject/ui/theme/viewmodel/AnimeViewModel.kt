package com.example.animeproject.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import com.example.animeproject.data.Anime
import com.example.animeproject.data.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnimeViewModel : ViewModel() {
    private val _animeList = MutableStateFlow(MockData.animeList)
    val animeList: StateFlow<List<Anime>> = _animeList

    fun getAnimeById(id: String): Anime? {
        return _animeList.value.find { it.id == id }
    }
}