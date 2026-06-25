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

data class ResultState(
    val isLoading: Boolean = true,
    val sheet: AnswerSheetEntity? = null,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val unanswered: Int = 0,
    val scorePercentage: Int = 0
)

class ResultsViewModel(private val repository: QFlowRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultState())
    val uiState: StateFlow<ResultState> = _uiState.asStateFlow()

    fun loadResults(sheetId: Int) {
        viewModelScope.launch {
            val sheet = repository.getSheetById(sheetId) ?: return@launch
            val rows = repository.getRowsForSheetSync(sheetId)
            
            var correct = 0
            var wrong = 0
            var unans = 0
            
            rows.forEach { row ->
                if (row.userAnswer == null) {
                    unans++
                } else if (row.correctAnswer != null && row.userAnswer == row.correctAnswer) {
                    correct++
                } else {
                    wrong++
                }
            }
            
            val total = rows.size
            val percentage = if (total > 0) (correct * 100) / total else 0
            
            _uiState.value = ResultState(
                isLoading = false,
                sheet = sheet,
                totalQuestions = total,
                correctAnswers = correct,
                wrongAnswers = wrong,
                unanswered = unans,
                scorePercentage = percentage
            )
        }
    }
}
