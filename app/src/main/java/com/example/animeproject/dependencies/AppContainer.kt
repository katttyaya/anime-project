package com.example.animeproject.dependencies

import android.content.Context
import com.example.animeproject.data.local.SettingsDataStore
import com.example.animeproject.data.local.database.AnimeDao
import com.example.animeproject.data.local.database.AppDatabase
import com.example.animeproject.data.network.RetrofitClient
import com.example.animeproject.data.repository.AnimeRepositoryImpl
import com.example.animeproject.domain.repository.AnimeRepository
import com.example.animeproject.domain.usecase.GetAnimeListUseCase

interface AppContainer {
    val settingsDataStore: SettingsDataStore
    val filterCache: FilterCache
    val animeDao: AnimeDao
    val animeRepository: AnimeRepository
    val getAnimeListUseCase: GetAnimeListUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val settingsDataStore: SettingsDataStore by lazy { SettingsDataStore(context) }
    override val filterCache: FilterCache by lazy { FilterCache() }

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    override val animeDao: AnimeDao by lazy { database.animeDao() }

    private val api by lazy { RetrofitClient.api }

    override val animeRepository: AnimeRepository by lazy {
        AnimeRepositoryImpl(api, animeDao)
    }

    override val getAnimeListUseCase: GetAnimeListUseCase by lazy {
        GetAnimeListUseCase(animeRepository)
    }
}