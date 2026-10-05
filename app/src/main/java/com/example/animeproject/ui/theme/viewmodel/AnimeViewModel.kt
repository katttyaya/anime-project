package com.example.animeproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.animeapp.data.Anime
import com.example.animeapp.data.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnimeViewModel : ViewModel() {
    // Поток со списком аниме
    private val _animeList = MutableStateFlow(MockData.animeList)
    val animeList: StateFlow<List<Anime>> = _animeList

    // Функция для поиска аниме по ID (для экрана деталей)
    fun getAnimeById(id: String): Anime? {
        return _animeList.value.find { it.id == id }
    }
}