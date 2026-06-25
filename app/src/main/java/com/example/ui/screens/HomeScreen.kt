package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.QFlowApplication
import com.example.data.local.AnswerSheetEntity
import com.example.ui.viewmodels.HomeViewModel
import com.example.ui.viewmodels.QFlowViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSetup: () -> Unit,
    onNavigateToSheet: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as QFlowApplication
    val factory = QFlowViewModelFactory(app.repository)
    val viewModel: HomeViewModel = viewModel(factory = factory)
    
    val sheets by viewModel.sheets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QFlow", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToSetup,
                icon = { Icon(Icons.Default.Add, "New Sheet") },
                text = { Text("New Sheet") }
            )
        }
    ) { padding ->
        if (sheets.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No sheets found. Create one to calculate scores!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sheets) { sheet ->
                    SheetCard(
                        sheet = sheet,
                        onClick = { onNavigateToSheet(sheet.id) },
                        onDelete = { viewModel.deleteSheet(sheet) }
                    )
                }
            }
        }
    }
}

@Composable
fun SheetCard(
    sheet: AnswerSheetEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = sheet.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${sheet.questionCount} Questions • ${sheet.optionsCount} Options", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Sheet", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
