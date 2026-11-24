package com.example.animeproject

import android.app.Application
import com.example.animeproject.dependencies.AppContainer
import com.example.animeproject.dependencies.DefaultAppContainer

class AnimeApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}