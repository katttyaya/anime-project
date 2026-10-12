package com.example.animeproject.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://kitsu.app/"

    fun create(context: Context): AnimeApiService {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()

        val client = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApiService::class.java)
    }
}