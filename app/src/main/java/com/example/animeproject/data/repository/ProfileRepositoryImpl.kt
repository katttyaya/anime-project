package com.example.animeproject.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.animeproject.domain.model.Profile
import com.example.animeproject.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore(name = "profile_preferences")

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {

    companion object {
        private val FULL_NAME = stringPreferencesKey("full_name")
        private val POSITION = stringPreferencesKey("position")
        private val AVATAR_URI = stringPreferencesKey("avatar_uri")
        private val RESUME_URL = stringPreferencesKey("resume_url")
    }

    override fun getProfileFlow(): Flow<Profile> =
        context.profileDataStore.data.map { prefs ->
            Profile(
                fullName = prefs[FULL_NAME] ?: "",
                position = prefs[POSITION] ?: "",
                avatarUri = prefs[AVATAR_URI] ?: "",
                resumeUrl = prefs[RESUME_URL] ?: ""
            )
        }

    override suspend fun saveProfile(profile: Profile) {
        context.profileDataStore.edit { prefs ->
            prefs[FULL_NAME] = profile.fullName
            prefs[POSITION] = profile.position
            prefs[AVATAR_URI] = profile.avatarUri
            prefs[RESUME_URL] = profile.resumeUrl
        }
    }

    override suspend fun clearProfile() {
        context.profileDataStore.edit { it.clear() }
    }
}