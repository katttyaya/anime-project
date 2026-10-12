package com.example.animeproject.data.network

import com.example.animeproject.data.model.KitsuResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeApiService {
    @GET("api/edge/anime")
    suspend fun getAnimeList(
        @Query("filter[text]") searchQuery: String = "",
        @Query("page[limit]") limit: Int = 10
    ): KitsuResponse
}