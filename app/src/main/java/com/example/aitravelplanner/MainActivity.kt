package com.example.aitravelplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.aitravelplanner.data.repository.PreferencesRepository
import com.example.aitravelplanner.ui.navigation.NavGraph
import com.example.aitravelplanner.ui.theme.AITravelPlannerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkModeFromStore by preferencesRepository.darkMode.collectAsState(initial = false)
            // Local state so UI updates immediately without waiting for DataStore write
            var darkTheme by remember(darkModeFromStore) { mutableStateOf(darkModeFromStore) }
            val navController = rememberNavController()

            AITravelPlannerTheme(darkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(
                        navController = navController,
                        darkTheme = darkTheme,
                        onToggleDarkTheme = { darkTheme = it }
                    )
                }
            }
        }
    }
}
