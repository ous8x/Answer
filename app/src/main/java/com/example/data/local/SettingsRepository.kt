package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "qflow_settings")

enum class ThemePreference { LIGHT, DARK, SYSTEM }
enum class TimerDuration { OFF, THREE_SEC, FOUR_SEC, FIVE_SEC }
enum class AnswerLabelStyle { ABCD, NUMBERS, NONE }

data class AppSettings(
    val theme: ThemePreference = ThemePreference.SYSTEM,
    val autoNext: Boolean = false,
    val timerDuration: TimerDuration = TimerDuration.OFF,
    val skipOnTimerEnd: Boolean = true,
    val showCorrectInstantly: Boolean = true,
    val showFinalResultOnly: Boolean = false,
    val labelStyle: AnswerLabelStyle = AnswerLabelStyle.ABCD,
    val enableQuestionNavigator: Boolean = true
)

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val AUTO_NEXT = booleanPreferencesKey("auto_next")
        val TIMER = stringPreferencesKey("timer")
        val SKIP_ON_END = booleanPreferencesKey("skip_on_end")
        val SHOW_CORRECT_INSTANTLY = booleanPreferencesKey("show_correct_instantly")
        val SHOW_FINAL_RESULT_ONLY = booleanPreferencesKey("show_final_result_only")
        val LABEL_STYLE = stringPreferencesKey("label_style")
        val ENABLE_NAVIGATOR = booleanPreferencesKey("enable_navigator")
    }

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            theme = ThemePreference.valueOf(prefs[Keys.THEME] ?: ThemePreference.SYSTEM.name),
            autoNext = prefs[Keys.AUTO_NEXT] ?: false,
            timerDuration = TimerDuration.valueOf(prefs[Keys.TIMER] ?: TimerDuration.OFF.name),
            skipOnTimerEnd = prefs[Keys.SKIP_ON_END] ?: true,
            showCorrectInstantly = prefs[Keys.SHOW_CORRECT_INSTANTLY] ?: true,
            showFinalResultOnly = prefs[Keys.SHOW_FINAL_RESULT_ONLY] ?: false,
            labelStyle = AnswerLabelStyle.valueOf(prefs[Keys.LABEL_STYLE] ?: AnswerLabelStyle.ABCD.name),
            enableQuestionNavigator = prefs[Keys.ENABLE_NAVIGATOR] ?: true
        )
    }

    suspend fun updateTheme(theme: ThemePreference) = dataStore.edit { it[Keys.THEME] = theme.name }
    suspend fun updateAutoNext(autoNext: Boolean) = dataStore.edit { it[Keys.AUTO_NEXT] = autoNext }
    suspend fun updateTimer(timer: TimerDuration) = dataStore.edit { it[Keys.TIMER] = timer.name }
    suspend fun updateSkipOnEnd(skip: Boolean) = dataStore.edit { it[Keys.SKIP_ON_END] = skip }
    suspend fun updateShowCorrectInstantly(show: Boolean) = dataStore.edit { it[Keys.SHOW_CORRECT_INSTANTLY] = show }
    suspend fun updateShowFinalResultOnly(show: Boolean) = dataStore.edit { it[Keys.SHOW_FINAL_RESULT_ONLY] = show }
    suspend fun updateLabelStyle(style: AnswerLabelStyle) = dataStore.edit { it[Keys.LABEL_STYLE] = style.name }
    suspend fun updateEnableNavigator(enable: Boolean) = dataStore.edit { it[Keys.ENABLE_NAVIGATOR] = enable }
}
