package com.example.aitravelplanner.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.local.entity.TripEntity
import com.example.aitravelplanner.data.model.TravelPlanRequest
import com.example.aitravelplanner.data.repository.TravelRepository
import com.example.aitravelplanner.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelPlannerViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {

    private val _itineraryState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val itineraryState: StateFlow<UiState<String>> = _itineraryState.asStateFlow()

    private val _saveState = MutableStateFlow<Boolean>(false)
    val saveState: StateFlow<Boolean> = _saveState.asStateFlow()

    fun generateItinerary(request: TravelPlanRequest) {
        travelRepository.generateTravelPlan(request)
            .onEach { state -> _itineraryState.value = state }
            .launchIn(viewModelScope)
    }

    fun saveTrip(
        request: TravelPlanRequest,
        itinerary: String
    ) {
        viewModelScope.launch {
            travelRepository.saveTrip(
                TripEntity(
                    title = "${request.departureCity} → ${request.destination}",
                    departureCity = request.departureCity,
                    destination = request.destination,
                    budgetUsd = request.budgetUsd,
                    days = request.days,
                    travelStyle = request.travelStyle,
                    itinerary = itinerary
                )
            )
            _saveState.value = true
        }
    }

    fun resetSaveState() {
        _saveState.value = false
    }

    fun resetItinerary() {
        _itineraryState.value = UiState.Idle
    }
}
