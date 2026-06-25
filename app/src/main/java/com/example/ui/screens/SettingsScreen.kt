package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.QFlowApplication
import com.example.data.local.ThemePreference
import com.example.data.local.TimerDuration
import com.example.ui.viewmodels.QFlowViewModelFactory
import com.example.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as QFlowApplication
    val factory = QFlowViewModelFactory(app.repository, app.settingsRepository)
    val viewModel: SettingsViewModel = viewModel(factory = factory)
    
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            ListItem(
                headlineContent = { Text("Auto-Next") },
                supportingContent = { Text("Automatically go to next question after selecting an answer (Single Choice).") },
                trailingContent = {
                    Switch(checked = settings.autoNext, onCheckedChange = { viewModel.updateAutoNext(it) })
                },
                modifier = Modifier.toggleable(
                    value = settings.autoNext,
                    role = Role.Switch,
                    onValueChange = { viewModel.updateAutoNext(it) }
                )
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Show Correct Answer Instantly") },
                supportingContent = { Text("Display if answer is correct before moving next.") },
                trailingContent = {
                    Switch(checked = settings.showCorrectInstantly, onCheckedChange = { viewModel.updateShowCorrectInstantly(it) })
                },
                modifier = Modifier.toggleable(
                    value = settings.showCorrectInstantly,
                    role = Role.Switch,
                    onValueChange = { viewModel.updateShowCorrectInstantly(it) }
                )
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Timer") },
                supportingContent = { Text("Time limit per question.") },
                trailingContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(when (settings.timerDuration) {
                                TimerDuration.OFF -> "Off"
                                TimerDuration.THREE_SEC -> "3s"
                                TimerDuration.FOUR_SEC -> "4s"
                                TimerDuration.FIVE_SEC -> "5s"
                            })
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("Off") }, onClick = { viewModel.updateTimer(TimerDuration.OFF); expanded = false })
                            DropdownMenuItem(text = { Text("3 Seconds") }, onClick = { viewModel.updateTimer(TimerDuration.THREE_SEC); expanded = false })
                            DropdownMenuItem(text = { Text("4 Seconds") }, onClick = { viewModel.updateTimer(TimerDuration.FOUR_SEC); expanded = false })
                            DropdownMenuItem(text = { Text("5 Seconds") }, onClick = { viewModel.updateTimer(TimerDuration.FIVE_SEC); expanded = false })
                        }
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Theme") },
                trailingContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(settings.theme.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("System") }, onClick = { viewModel.updateTheme(ThemePreference.SYSTEM); expanded = false })
                            DropdownMenuItem(text = { Text("Light") }, onClick = { viewModel.updateTheme(ThemePreference.LIGHT); expanded = false })
                            DropdownMenuItem(text = { Text("Dark") }, onClick = { viewModel.updateTheme(ThemePreference.DARK); expanded = false })
                        }
                    }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Skip on Timer End") },
                trailingContent = {
                    Switch(checked = settings.skipOnTimerEnd, onCheckedChange = { viewModel.updateSkipOnEnd(it) })
                },
                modifier = Modifier.toggleable(
                    value = settings.skipOnTimerEnd,
                    role = Role.Switch,
                    onValueChange = { viewModel.updateSkipOnEnd(it) }
                )
            )
        }
    }
}
