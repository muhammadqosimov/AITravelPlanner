package com.example.aitravelplanner.data.model

data class TravelPlanRequest(
    val departureCity: String,
    val destination: String,
    val budgetUsd: Int,
    val days: Int,
    val travelStyle: String
)

data class DestinationRequest(
    val budgetUsd: Int,
    val preferredWeather: String = "",
    val durationDays: Int = 0,
    val travelStyle: String = "",
    val surpriseMode: Boolean = false
)
