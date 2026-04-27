package com.example.aitravelplanner.ui.navigation

sealed class Screen(val route: String) {
    data object Splash          : Screen("splash")
    data object Home            : Screen("home")
    data object TravelPlanner   : Screen("travel_planner")
    data object DestinationFinder : Screen("destination_finder")
    data object FlightBooking   : Screen("flight_booking")
    data object SavedTrips      : Screen("saved_trips")
    data object Settings        : Screen("settings")
    data object About           : Screen("about")
}
