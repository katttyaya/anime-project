package com.example.animeproject.domain.repository

import com.example.animeproject.domain.model.Anime

interface AnimeRepository {
    suspend fun getAnimeList(query: String): List<Anime>
    suspend fun getAnimeById(id: String): Anime?
}