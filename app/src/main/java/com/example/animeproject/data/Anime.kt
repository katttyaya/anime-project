package com.example.animeproject.data

data class Anime(
    val id: String,
    val title: String,
    val posterResId: Int,
    val rating: String,
    val episodes: Int,
    val status: String,
    val synopsis: String
)