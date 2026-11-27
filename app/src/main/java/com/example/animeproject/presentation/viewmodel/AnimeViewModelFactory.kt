package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.animeproject.dependencies.AppContainer

class AnimeViewModelFactory(
    private val container: AppContainer,
    private val animeId: String = ""
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = when {
        modelClass.isAssignableFrom(AnimeListViewModel::class.java) ->
            AnimeListViewModel(
                container.animeRepository,
                container.settingsDataStore,
                container.filterCache
            ) as T

        modelClass.isAssignableFrom(AnimeDetailsViewModel::class.java) ->
            AnimeDetailsViewModel(
                container.animeRepository,
                animeId
            ) as T

        modelClass.isAssignableFrom(FavoritesViewModel::class.java) ->
            FavoritesViewModel(container.animeRepository) as T

        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(container.settingsDataStore, container.filterCache) as T

        modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
            ProfileViewModel(container.profileRepository) as T

        modelClass.isAssignableFrom(EditProfileViewModel::class.java) ->
            EditProfileViewModel(container.profileRepository) as T

        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}