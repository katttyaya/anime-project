package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.domain.model.Profile
import com.example.animeproject.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _position = MutableStateFlow("")
    val position: StateFlow<String> = _position.asStateFlow()

    private val _avatarUri = MutableStateFlow("")
    val avatarUri: StateFlow<String> = _avatarUri.asStateFlow()

    private val _resumeUrl = MutableStateFlow("")
    val resumeUrl: StateFlow<String> = _resumeUrl.asStateFlow()

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges: StateFlow<Boolean> = _hasChanges.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getProfileFlow().first()
            _fullName.value = profile.fullName
            _position.value = profile.position
            _avatarUri.value = profile.avatarUri
            _resumeUrl.value = profile.resumeUrl
        }
    }

    fun onFullNameChanged(name: String) {
        _fullName.value = name
        _hasChanges.value = true
    }

    fun onPositionChanged(position: String) {
        _position.value = position
        _hasChanges.value = true
    }

    fun onAvatarUriChanged(uri: String) {
        _avatarUri.value = uri
        _hasChanges.value = true
    }

    fun onResumeUrlChanged(url: String) {
        _resumeUrl.value = url
        _hasChanges.value = true
    }

    fun saveProfile(onSaved: () -> Unit) {
        viewModelScope.launch {
            val profile = Profile(
                fullName = _fullName.value,
                position = _position.value,
                avatarUri = _avatarUri.value,
                resumeUrl = _resumeUrl.value
            )
            profileRepository.saveProfile(profile)
            _hasChanges.value = false
            onSaved()
        }
    }

    fun resetChanges() {
        loadProfile()
        _hasChanges.value = false
    }
}