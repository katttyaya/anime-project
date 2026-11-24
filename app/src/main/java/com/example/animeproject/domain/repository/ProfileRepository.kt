package com.example.animeproject.domain.repository

import com.example.animeproject.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileFlow(): Flow<Profile>

    suspend fun saveProfile(profile: Profile)

    suspend fun clearProfile()
}