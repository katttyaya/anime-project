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

    private val _favoritePairTime = MutableStateFlow("")
    val favoritePairTime: StateFlow<String> = _favoritePairTime.asStateFlow()

    private val _timeError = MutableStateFlow<String?>(null)
    val timeError: StateFlow<String?> = _timeError.asStateFlow()

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges: StateFlow<Boolean> = _hasChanges.asStateFlow()

    private val _isSaveEnabled = MutableStateFlow(true)
    val isSaveEnabled: StateFlow<Boolean> = _isSaveEnabled.asStateFlow()

    // Флаг: нужно ли отменить будильник (если время было очищено)
    private val _shouldCancelAlarm = MutableStateFlow(false)
    val shouldCancelAlarm: StateFlow<Boolean> = _shouldCancelAlarm.asStateFlow()

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
            _favoritePairTime.value = profile.favoritePairTime
            _shouldCancelAlarm.value = false
            validateTime(profile.favoritePairTime)
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

    fun onFavoritePairTimeChanged(time: String) {
        _favoritePairTime.value = time
        _hasChanges.value = true

        if (time.isBlank() && _favoritePairTime.value.isNotBlank()) {
            _shouldCancelAlarm.value = true
        }

        validateTime(time)
    }

    fun clearFavoritePairTime() {
        _favoritePairTime.value = ""
        _shouldCancelAlarm.value = true
        _hasChanges.value = true
        validateTime("")
    }

    private fun validateTime(time: String) {
        if (time.isBlank()) {
            _timeError.value = null
            _isSaveEnabled.value = true
            return
        }

        val regex = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
        if (regex.matches(time)) {
            _timeError.value = null
            _isSaveEnabled.value = true
        } else {
            _timeError.value = "Некорректный формат времени"
            _isSaveEnabled.value = false
        }
    }

    fun saveProfile(onSaved: () -> Unit) {
        if (!_isSaveEnabled.value) return

        viewModelScope.launch {
            val profile = Profile(
                fullName = _fullName.value,
                position = _position.value,
                avatarUri = _avatarUri.value,
                resumeUrl = _resumeUrl.value,
                favoritePairTime = _favoritePairTime.value
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