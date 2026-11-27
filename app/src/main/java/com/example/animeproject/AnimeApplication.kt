package com.example.animeproject

import android.app.Application
import com.example.animeproject.dependencies.AppContainer
import com.example.animeproject.dependencies.DefaultAppContainer
import com.example.animeproject.util.NotificationHelper

class AnimeApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)

        NotificationHelper.createNotificationChannel(this)
    }
}