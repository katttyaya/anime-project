package com.example.animeproject.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animeproject.domain.model.Anime

@Entity(tableName = "favorite_anime")
data class AnimeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val synopsis: String,
    val rating: String,
    val episodes: Int,
    val status: String,
    val posterUrl: String
)

fun AnimeEntity.toDomainModel(): Anime = Anime(
    id = id, title = title, synopsis = synopsis, rating = rating,
    episodes = episodes, status = status, posterUrl = posterUrl
)

fun Anime.toEntity(): AnimeEntity = AnimeEntity(
    id = id, title = title, synopsis = synopsis, rating = rating,
    episodes = episodes, status = status, posterUrl = posterUrl
)