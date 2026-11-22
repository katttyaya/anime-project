package com.example.animeproject.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.data.network.RetrofitClient
import com.example.animeproject.data.repository.AnimeRepositoryImpl
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.usecase.GetAnimeListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Anime>) : UiState()
    data class Error(val message: String) : UiState()
}

class AnimeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnimeRepositoryImpl(RetrofitClient.create(application))
    private val useCase = GetAnimeListUseCase(repository)

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var _cachedAnimeList: List<Anime> = emptyList()

    init {
        loadAnimeList("cowboy")
    }

    fun loadAnimeList(query: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = useCase(query)
                _cachedAnimeList = result
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun getAnimeById(id: String): Anime? {
        return _cachedAnimeList.find { it.id == id }
    }
}