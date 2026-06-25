package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.local.ThemePreference
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as QFlowApplication
            val settings by app.settingsRepository.settingsFlow.collectAsState(initial = com.example.data.local.AppSettings())
            
            val isDarkTheme = when (settings.theme) {
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
            }
            
            AppTheme(darkTheme = isDarkTheme) {
                AppNavigation()
            }
        }
    }
}
