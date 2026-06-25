package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppSettings
import com.example.data.local.SettingsRepository
import com.example.data.local.ThemePreference
import com.example.data.local.TimerDuration
import com.example.data.local.AnswerLabelStyle
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
    fun updateAutoNext(autoNext: Boolean) = viewModelScope.launch { repository.updateAutoNext(autoNext) }
    fun updateTimer(timer: TimerDuration) = viewModelScope.launch { repository.updateTimer(timer) }
    fun updateSkipOnEnd(skip: Boolean) = viewModelScope.launch { repository.updateSkipOnEnd(skip) }
    fun updateShowCorrectInstantly(show: Boolean) = viewModelScope.launch { repository.updateShowCorrectInstantly(show) }
    fun updateShowFinalResultOnly(show: Boolean) = viewModelScope.launch { repository.updateShowFinalResultOnly(show) }
    fun updateLabelStyle(style: AnswerLabelStyle) = viewModelScope.launch { repository.updateLabelStyle(style) }
    fun updateEnableNavigator(enable: Boolean) = viewModelScope.launch { repository.updateEnableNavigator(enable) }
}
