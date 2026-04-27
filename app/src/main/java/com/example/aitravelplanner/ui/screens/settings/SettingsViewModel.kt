package com.example.aitravelplanner.ui.screens.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.repository.PreferencesRepository
import com.example.aitravelplanner.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val travelRepository: TravelRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val darkMode: StateFlow<Boolean> = preferencesRepository.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val language: StateFlow<String> = preferencesRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "en")

    private val _dataCleared = MutableStateFlow(false)
    val dataCleared: StateFlow<Boolean> = _dataCleared.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDarkMode(enabled)
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch {
            preferencesRepository.setLanguage(code)
            val localeList = LocaleListCompat.forLanguageTags(code)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            travelRepository.deleteAllTrips()
            travelRepository.deleteAllDestinations()
            _dataCleared.value = true
        }
    }

    fun resetDataCleared() { _dataCleared.value = false }
}
