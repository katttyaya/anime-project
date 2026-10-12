package com.example.animeproject.domain.model

data class Anime(
    val id: String,
    val title: String,
    val synopsis: String,
    val rating: String,
    val episodes: Int,
    val status: String,
    val posterUrl: String
)