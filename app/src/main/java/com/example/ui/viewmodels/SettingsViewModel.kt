package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppSettings
import com.example.data.local.SettingsRepository
import com.example.data.local.ThemePreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    
    val settings: StateFlow<AppSettings> = repository.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun updateTheme(theme: ThemePreference) = viewModelScope.launch { repository.updateTheme(theme) }
    fun updateDefaultQuestionCount(count: Int) = viewModelScope.launch { repository.updateDefaultQuestionCount(count) }
    fun updateDefaultOptionsCount(count: Int) = viewModelScope.launch { repository.updateDefaultOptionsCount(count) }
    fun updateAllowMultiSelect(allow: Boolean) = viewModelScope.launch { repository.updateAllowMultiSelect(allow) }
}
