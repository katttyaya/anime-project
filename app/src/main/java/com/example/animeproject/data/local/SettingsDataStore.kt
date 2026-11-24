package com.example.animeproject.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings_preferences")

class SettingsDataStore(private val context: Context) {
    object Keys {
        val SEARCH = stringPreferencesKey("search_query")
        val MIN_RATING = floatPreferencesKey("min_rating")
        val STATUS = stringPreferencesKey("status_filter")
    }

    val searchQueryFlow: Flow<String> = context.dataStore.data.map { it[Keys.SEARCH] ?: "" }
    val minRatingFlow: Flow<Float> = context.dataStore.data.map { it[Keys.MIN_RATING] ?: 0f }
    val statusFilterFlow: Flow<String> = context.dataStore.data.map { it[Keys.STATUS] ?: "Any" }

    suspend fun saveSettings(search: String, minRating: Float, status: String) {
        context.dataStore.edit {
            it[Keys.SEARCH] = search
            it[Keys.MIN_RATING] = minRating
            it[Keys.STATUS] = status
        }
    }
}