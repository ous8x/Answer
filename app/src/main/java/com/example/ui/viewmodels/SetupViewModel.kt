package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QFlowRepository
import com.example.data.local.LabelStyle
import kotlinx.coroutines.launch

class SetupViewModel(private val repository: QFlowRepository) : ViewModel() {

    suspend fun createNewSheet(title: String, questions: Int, options: Int, labelStyle: LabelStyle): Int {
        return repository.createSheet(title, questions, options, labelStyle)
    }
}
