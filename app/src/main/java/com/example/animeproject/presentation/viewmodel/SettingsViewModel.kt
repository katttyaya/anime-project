package com.example.animeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeproject.data.local.SettingsDataStore
import com.example.animeproject.dependencies.FilterCache
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val filterCache: FilterCache
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _minRating = MutableStateFlow(0f)
    val minRating: StateFlow<Float> = _minRating.asStateFlow()

    private val _statusFilter = MutableStateFlow("Any")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery.value = settingsDataStore.searchQueryFlow.first()
            _minRating.value = settingsDataStore.minRatingFlow.first()
            _statusFilter.value = settingsDataStore.statusFilterFlow.first()
        }
    }

    fun onSearchChanged(q: String) { _searchQuery.value = q }
    fun onMinRatingChanged(r: Float) { _minRating.value = r }
    fun onStatusChanged(s: String) { _statusFilter.value = s }

    fun saveSettings(onSaved: () -> Unit) {
        viewModelScope.launch {
            settingsDataStore.saveSettings(_searchQuery.value, _minRating.value, _statusFilter.value)
            filterCache.updateFiltersState(_searchQuery.value, _minRating.value, _statusFilter.value)
            onSaved()
        }
    }

    fun resetSettings(onReset: () -> Unit) {
        viewModelScope.launch {
            _searchQuery.value = ""
            _minRating.value = 0f
            _statusFilter.value = "Any"
            settingsDataStore.saveSettings("", 0f, "Any")
            filterCache.updateFiltersState("", 0f, "Any")
            onReset()
        }
    }
}