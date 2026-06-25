package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.QFlowRepository
import com.example.data.local.SettingsRepository

class QFlowViewModelFactory(
    private val repository: QFlowRepository,
    private val settingsRepository: SettingsRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java) && settingsRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(SetupViewModel::class.java) && settingsRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return SetupViewModel(repository, settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(AnswerSheetViewModel::class.java) && settingsRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return AnswerSheetViewModel(repository, settingsRepository) as T
        }
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
