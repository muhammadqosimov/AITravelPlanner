package com.example.aitravelplanner.data.repository

import com.example.aitravelplanner.data.model.Flight
import com.example.aitravelplanner.data.model.MockFlightData
import com.example.aitravelplanner.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightRepository @Inject constructor() {

    fun searchFlights(from: String, to: String, date: String): Flow<UiState<List<Flight>>> = flow {
        emit(UiState.Loading)
        // Simulate network delay for realistic UX
        delay(1500)
        try {
            val flights = MockFlightData.generateFlights(from, to, date)
            emit(UiState.Success(flights))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to search flights: ${e.message}"))
        }
    }
}
