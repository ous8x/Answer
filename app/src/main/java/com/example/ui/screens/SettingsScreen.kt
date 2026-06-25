package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.QFlowApplication
import com.example.data.local.ThemePreference
import com.example.ui.viewmodels.QFlowViewModelFactory
import com.example.ui.viewmodels.SettingsViewModel
import kotlin.math.roundToInt

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
                headlineContent = { Text("Default Question Count: ${settings.defaultQuestionCount}") },
                supportingContent = {
                    Slider(
                        value = settings.defaultQuestionCount.toFloat(),
                        onValueChange = { viewModel.updateDefaultQuestionCount(it.roundToInt()) },
                        valueRange = 1f..150f,
                        steps = 149
                    )
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Default Options Count: ${settings.defaultOptionsCount}") },
                supportingContent = {
                    Slider(
                        value = settings.defaultOptionsCount.toFloat(),
                        onValueChange = { viewModel.updateDefaultOptionsCount(it.roundToInt()) },
                        valueRange = 2f..10f,
                        steps = 8
                    )
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Allow Multiple Answers") },
                supportingContent = { Text("If disabled, only one option can be selected per question.") },
                trailingContent = {
                    Switch(checked = settings.allowMultiSelect, onCheckedChange = { viewModel.updateAllowMultiSelect(it) })
                },
                modifier = Modifier.toggleable(
                    value = settings.allowMultiSelect,
                    role = Role.Switch,
                    onValueChange = { viewModel.updateAllowMultiSelect(it) }
                )
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
        }
    }
}
