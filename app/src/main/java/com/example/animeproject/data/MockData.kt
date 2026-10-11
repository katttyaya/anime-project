package com.example.animeproject.data

import com.example.animeproject.R

object MockData {
    val animeList = listOf(
        Anime(
            id = "1",
            title = "Аниме 1",
            posterResId = R.drawable.anime1,
            rating = "82.27",
            episodes = 26,
            status = "Завершено",
            synopsis = "Описание первого аниме..."
        ),
        Anime(
            id = "2",
            title = "Аниме 2",
            posterResId = R.drawable.anime2,
            rating = "84.50",
            episodes = 25,
            status = "Завершено",
            synopsis = "Описание второго аниме..."
        ),
        Anime(
            id = "3",
            title = "Манга",
            posterResId = R.drawable.anime1,
            rating = "86.10",
            episodes = 37,
            status = "Завершено",
            synopsis = "Описание манги..."
        )
    )
}