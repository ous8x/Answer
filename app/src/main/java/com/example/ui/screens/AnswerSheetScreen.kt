package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.QFlowApplication
import com.example.data.local.LabelStyle
import com.example.data.local.SheetRowEntity
import com.example.ui.viewmodels.AnswerSheetViewModel
import com.example.ui.viewmodels.QFlowViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerSheetScreen(
    sheetId: Int,
    onNavigateBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as QFlowApplication
    val factory = QFlowViewModelFactory(app.repository, app.settingsRepository)
    val viewModel: AnswerSheetViewModel = viewModel(factory = factory)
    
    LaunchedEffect(sheetId) {
        viewModel.loadSheet(sheetId)
    }
    
    val state by viewModel.uiState.collectAsState()
    val sheet = state.sheet

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sheet?.title ?: "Answer Sheet") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { viewModel.setEditingMode(!state.isEditingKey) }) {
                        Text(if (state.isEditingKey) "Switch to My Answers" else "Switch to Correct Key")
                    }
                    Button(onClick = { onFinish(sheetId) }) {
                        Text("Calculate Score")
                    }
                }
            }
        }
    ) { padding ->
        if (sheet == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(if (state.isEditingKey) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer).padding(16.dp)
                ) {
                    Text(
                        text = if (state.isEditingKey) "Editing: Correct Answer Key" else "Editing: User Answers",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (state.isEditingKey) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.rows) { row ->
                        SheetRowItem(
                            row = row,
                            optionsCount = sheet.optionsCount,
                            labelStyle = sheet.labelStyle,
                            isEditingKey = state.isEditingKey,
                            onOptionSelected = { optIdx -> viewModel.selectOption(row, optIdx) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SheetRowItem(
    row: SheetRowEntity,
    optionsCount: Int,
    labelStyle: LabelStyle,
    isEditingKey: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    val correctAnswers = row.correctAnswer?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }?.toSet() ?: emptySet()
    val userAnswers = row.userAnswer?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }?.toSet() ?: emptySet()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${row.questionNumber}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until optionsCount) {
                val label = when (labelStyle) {
                    LabelStyle.ABCD -> "${(65 + i).toChar()}"
                    LabelStyle.NUMBERS -> "${i + 1}"
                    LabelStyle.TRUE_FALSE -> if (i == 0) "T" else "F"
                }
                
                val isSelected = if (isEditingKey) correctAnswers.contains(i) else userAnswers.contains(i)
                val color = if (isSelected) {
                    if (isEditingKey) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
                val contentColor = if (isSelected) {
                    if (isEditingKey) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onOptionSelected(i) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, color = contentColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
