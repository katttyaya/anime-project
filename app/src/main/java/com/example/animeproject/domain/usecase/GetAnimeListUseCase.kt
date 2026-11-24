package com.example.animeproject.domain.usecase

import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository

class GetAnimeListUseCase(private val repository: AnimeRepository) {
    suspend operator fun invoke(query: String = "", limit: Int = 15): List<Anime> =
        repository.getAnimeList(query, limit)
}