package com.example.animeproject.domain.model

data class Profile(
    val fullName: String = "",      // ФИО
    val position: String = "",      // Должность
    val avatarUri: String = "",     // URI аватарки (content:// или file://)
    val resumeUrl: String = ""      // URL резюме (https://...)
)