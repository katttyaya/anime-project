package com.example.animeproject.data.repository

import com.example.animeproject.data.local.database.AnimeDao
import com.example.animeproject.data.local.database.toDomainModel
import com.example.animeproject.data.local.database.toEntity
import com.example.animeproject.data.network.AnimeApiService
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnimeRepositoryImpl(
    private val apiService: AnimeApiService,
    private val animeDao: AnimeDao
) : AnimeRepository {

    override suspend fun getAnimeList(query: String, limit: Int): List<Anime> {
        val response = apiService.getAnimeList(searchQuery = query, limit = limit)
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

    override suspend fun getAnimeById(id: String): Anime? {
        val local = animeDao.getFavoriteById(id)?.toDomainModel()
        if (local != null) return local
        val list = getAnimeList(id)
        return list.find { it.id == id }
    }

    override fun getFavoritesFlow(): Flow<List<Anime>> =
        animeDao.getAllFavoritesFlow().map { it.map { e -> e.toDomainModel() } }

    override suspend fun toggleFavorite(anime: Anime) {
        if (animeDao.getFavoriteById(anime.id) != null)
            animeDao.deleteFavoriteById(anime.id)
        else
            animeDao.insertFavorite(anime.toEntity())
    }

    override fun isFavoriteFlow(id: String): Flow<Boolean> =
        animeDao.isFavoriteFlow(id)

    override suspend fun getFavoriteById(id: String): Anime? =
        animeDao.getFavoriteById(id)?.toDomainModel()
}