package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QFlowRepository
import com.example.data.local.AnswerSheetEntity
import com.example.data.local.SettingsRepository
import com.example.data.local.SheetRowEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AnswerSheetState(
    val sheet: AnswerSheetEntity? = null,
    val rows: List<SheetRowEntity> = emptyList(),
    val isEditingKey: Boolean = false
)

class AnswerSheetViewModel(
    private val repository: QFlowRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnswerSheetState())
    val uiState: StateFlow<AnswerSheetState> = _uiState.asStateFlow()

    private val _allowMultiSelect = settingsRepository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
        val allowMulti = _allowMultiSelect.value?.allowMultiSelect ?: true

        val currentAnswers = if (currentState.isEditingKey) {
            row.correctAnswer?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()
        } else {
            row.userAnswer?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }?.toMutableSet() ?: mutableSetOf()
        }

        if (allowMulti) {
            if (currentAnswers.contains(optionIndex)) {
                currentAnswers.remove(optionIndex)
            } else {
                currentAnswers.add(optionIndex)
            }
        } else {
            if (currentAnswers.contains(optionIndex)) {
                currentAnswers.clear() // unselect
            } else {
                currentAnswers.clear()
                currentAnswers.add(optionIndex)
            }
        }

        val newAnswerStr = if (currentAnswers.isEmpty()) null else currentAnswers.sorted().joinToString(",")

        val updatedRow = if (currentState.isEditingKey) {
            row.copy(correctAnswer = newAnswerStr)
        } else {
            row.copy(userAnswer = newAnswerStr)
        }

        viewModelScope.launch {
            repository.updateRow(updatedRow)
        }
    }
}
