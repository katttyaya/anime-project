package com.example.animeproject.domain.repository

import com.example.animeproject.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(query: String, limit: Int = 15): List<Anime>
    suspend fun getAnimeById(id: String): Anime?
    fun getFavoritesFlow(): Flow<List<Anime>>
    suspend fun toggleFavorite(anime: Anime)
    fun isFavoriteFlow(id: String): Flow<Boolean>
    suspend fun getFavoriteById(id: String): Anime?
}