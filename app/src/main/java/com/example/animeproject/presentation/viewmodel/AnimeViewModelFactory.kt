package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.animeproject.domain.repository.AnimeRepository

class AnimeViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(AnimeListViewModel::class.java) -> {
                AnimeListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AnimeDetailsViewModel::class.java) -> {
                AnimeDetailsViewModel(repository, androidx.lifecycle.SavedStateHandle()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}