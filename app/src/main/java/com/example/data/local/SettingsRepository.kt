package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "qflow_settings")

enum class ThemePreference { LIGHT, DARK, SYSTEM }

data class AppSettings(
    val theme: ThemePreference = ThemePreference.SYSTEM,
    val defaultQuestionCount: Int = 40,
    val defaultOptionsCount: Int = 4,
    val allowMultiSelect: Boolean = true
)

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val DEFAULT_Q_COUNT = intPreferencesKey("default_q_count")
        val DEFAULT_OPT_COUNT = intPreferencesKey("default_opt_count")
        val ALLOW_MULTI_SELECT = booleanPreferencesKey("allow_multi_select")
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            theme = ThemePreference.valueOf(prefs[Keys.THEME] ?: ThemePreference.SYSTEM.name),
            defaultQuestionCount = prefs[Keys.DEFAULT_Q_COUNT] ?: 40,
            defaultOptionsCount = prefs[Keys.DEFAULT_OPT_COUNT] ?: 4,
            allowMultiSelect = prefs[Keys.ALLOW_MULTI_SELECT] ?: true
        )
    }

    suspend fun updateTheme(theme: ThemePreference) = dataStore.edit { it[Keys.THEME] = theme.name }
    suspend fun updateDefaultQuestionCount(count: Int) = dataStore.edit { it[Keys.DEFAULT_Q_COUNT] = count }
    suspend fun updateDefaultOptionsCount(count: Int) = dataStore.edit { it[Keys.DEFAULT_OPT_COUNT] = count }
    suspend fun updateAllowMultiSelect(allow: Boolean) = dataStore.edit { it[Keys.ALLOW_MULTI_SELECT] = allow }
}
