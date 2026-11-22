package com.example.animeproject.dependencies

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterCache {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    fun updateFiltersState(searchQuery: String, minRating: Float, statusFilter: String) {
        val isActive = searchQuery.isNotBlank() || minRating > 0f || statusFilter != "Any"
        _hasActiveFilters.value = isActive
    }
}