package com.example.animeproject.data.repository

import com.example.animeproject.data.network.AnimeApiService
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository

class AnimeRepositoryImpl(
    private val apiService: AnimeApiService
) : AnimeRepository {
    override suspend fun getAnimeList(query: String): List<Anime> {
        val response = apiService.getAnimeList(searchQuery = query, limit = 15)
        return response.data.map { dto ->
            Anime(
                id = dto.id,
                title = dto.attributes.title,
                synopsis = dto.attributes.synopsis,
                rating = dto.attributes.rating ?: "N/A",
                episodes = dto.attributes.episodes ?: 0,
                status = dto.attributes.status,
                posterUrl = dto.attributes.posterImage?.medium ?: ""
            )
        }
    }
}