package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QFlowRepository
import com.example.data.local.AnswerSheetEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: QFlowRepository) : ViewModel() {
    val sheets: StateFlow<List<AnswerSheetEntity>> = repository.allSheets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSheet(sheet: AnswerSheetEntity) {
        viewModelScope.launch {
            repository.deleteSheet(sheet)
        }
    }
}
