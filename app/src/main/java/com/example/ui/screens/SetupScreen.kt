package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.QFlowApplication
import com.example.data.local.LabelStyle
import com.example.ui.viewmodels.QFlowViewModelFactory
import com.example.ui.viewmodels.SetupViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    onSheetCreated: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as QFlowApplication
    val factory = QFlowViewModelFactory(app.repository, app.settingsRepository)
    val viewModel: SetupViewModel = viewModel(factory = factory)
    
    val settings by viewModel.settings.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var title by remember { mutableStateOf("New Sheet") }
    var qCount by remember { mutableFloatStateOf(0f) }
    var optCount by remember { mutableFloatStateOf(0f) }
    var labelStyle by remember { mutableStateOf(LabelStyle.ABCD) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(settings) {
        if (!initialized && settings.defaultQuestionCount > 0) {
            qCount = settings.defaultQuestionCount.toFloat()
            optCount = settings.defaultOptionsCount.toFloat()
            initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sheet Setup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Sheet Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Total Questions: ${qCount.toInt()}", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = if (qCount == 0f) 40f else qCount,
                onValueChange = { qCount = it },
                valueRange = 1f..150f,
                steps = 149
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (labelStyle != LabelStyle.TRUE_FALSE) {
                Text("Options per Question: ${optCount.toInt()}", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = if (optCount == 0f) 4f else optCount,
                    onValueChange = { optCount = it },
                    valueRange = 2f..10f,
                    steps = 8
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Label Style", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = labelStyle == LabelStyle.ABCD,
                    onClick = { labelStyle = LabelStyle.ABCD; if(optCount < 2) optCount = 4f },
                    label = { Text("A B C D") }
                )
                FilterChip(
                    selected = labelStyle == LabelStyle.NUMBERS,
                    onClick = { labelStyle = LabelStyle.NUMBERS; if(optCount < 2) optCount = 4f },
                    label = { Text("1 2 3 4") }
                )
                FilterChip(
                    selected = labelStyle == LabelStyle.TRUE_FALSE,
                    onClick = { labelStyle = LabelStyle.TRUE_FALSE; optCount = 2f },
                    label = { Text("T / F") }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        coroutineScope.launch {
                            val id = viewModel.createNewSheet(title, qCount.toInt(), optCount.toInt(), labelStyle)
                            onSheetCreated(id)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Generate Sheet")
            }
        }
    }
}
