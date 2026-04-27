package com.example.aitravelplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aitravelplanner.ui.screens.about.AboutScreen
import com.example.aitravelplanner.ui.screens.destination.DestinationFinderScreen
import com.example.aitravelplanner.ui.screens.flight.FlightBookingScreen
import com.example.aitravelplanner.ui.screens.home.HomeScreen
import com.example.aitravelplanner.ui.screens.planner.TravelPlannerScreen
import com.example.aitravelplanner.ui.screens.saved.SavedTripsScreen
import com.example.aitravelplanner.ui.screens.settings.SettingsScreen
import com.example.aitravelplanner.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    darkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateTo = { screen -> navController.navigate(screen.route) }
            )
        }

        composable(Screen.TravelPlanner.route) {
            TravelPlannerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DestinationFinder.route) {
            DestinationFinderScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.FlightBooking.route) {
            FlightBookingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SavedTrips.route) {
            SavedTripsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                darkTheme = darkTheme,
                onToggleDarkTheme = onToggleDarkTheme,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
