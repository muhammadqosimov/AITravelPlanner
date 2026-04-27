package com.example.aitravelplanner.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val darkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }

    val language: Flow<String> = dataStore.data.map { it[LANGUAGE_KEY] ?: "en" }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    suspend fun setLanguage(code: String) {
        dataStore.edit { it[LANGUAGE_KEY] = code }
    }
}
