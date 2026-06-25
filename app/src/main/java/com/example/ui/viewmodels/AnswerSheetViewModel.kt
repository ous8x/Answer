package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QFlowRepository
import com.example.data.local.AnswerSheetEntity
import com.example.data.local.SheetRowEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnswerSheetState(
    val sheet: AnswerSheetEntity? = null,
    val rows: List<SheetRowEntity> = emptyList(),
    val isEditingKey: Boolean = false // false means editing user answers, true means editing correct answers
)

class AnswerSheetViewModel(private val repository: QFlowRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AnswerSheetState())
    val uiState: StateFlow<AnswerSheetState> = _uiState.asStateFlow()

    fun loadSheet(sheetId: Int) {
        viewModelScope.launch {
            val sheet = repository.getSheetById(sheetId)
            repository.getRowsForSheet(sheetId).collect { rows ->
                _uiState.value = _uiState.value.copy(sheet = sheet, rows = rows)
            }
        }
    }

    fun setEditingMode(isEditingKey: Boolean) {
        _uiState.value = _uiState.value.copy(isEditingKey = isEditingKey)
    }

    fun selectOption(row: SheetRowEntity, optionIndex: Int) {
        val currentState = _uiState.value
        val updatedRow = if (currentState.isEditingKey) {
            // Toggle off if already selected, otherwise set
            val newAnswer = if (row.correctAnswer == optionIndex) null else optionIndex
            row.copy(correctAnswer = newAnswer)
        } else {
            val newAnswer = if (row.userAnswer == optionIndex) null else optionIndex
            row.copy(userAnswer = newAnswer)
        }
        viewModelScope.launch {
            repository.updateRow(updatedRow)
        }
    }
}
