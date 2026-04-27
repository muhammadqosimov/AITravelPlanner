package com.example.aitravelplanner.ui.screens.flight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.model.Flight
import com.example.aitravelplanner.data.repository.FlightRepository
import com.example.aitravelplanner.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FlightBookingViewModel @Inject constructor(
    private val flightRepository: FlightRepository
) : ViewModel() {

    private val _flightsState = MutableStateFlow<UiState<List<Flight>>>(UiState.Idle)
    val flightsState: StateFlow<UiState<List<Flight>>> = _flightsState.asStateFlow()

    private val _bookedFlight = MutableStateFlow<Flight?>(null)
    val bookedFlight: StateFlow<Flight?> = _bookedFlight.asStateFlow()

    fun searchFlights(from: String, to: String, date: String) {
        flightRepository.searchFlights(from, to, date)
            .onEach { _flightsState.value = it }
            .launchIn(viewModelScope)
    }

    fun bookFlight(flight: Flight) {
        _bookedFlight.value = flight
    }

    fun dismissBookingConfirmation() {
        _bookedFlight.value = null
    }

    fun reset() {
        _flightsState.value = UiState.Idle
    }
}
