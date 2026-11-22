package com.example.animeproject.data.model

import com.google.gson.annotations.SerializedName

data class KitsuResponse(
    val data: List<AnimeData>
)

data class AnimeData(
    val id: String,
    val attributes: AnimeAttributes
)

data class AnimeAttributes(
    @SerializedName("canonicalTitle")
    val title: String,
    val synopsis: String,
    @SerializedName("averageRating")
    val rating: String?,
    @SerializedName("episodeCount")
    val episodes: Int?,
    val status: String,
    @SerializedName("posterImage")
    val posterImage: PosterImage?
)

data class PosterImage(
    val medium: String?
)