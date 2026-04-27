package com.example.aitravelplanner.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.local.entity.DestinationEntity
import com.example.aitravelplanner.data.local.entity.TripEntity
import com.example.aitravelplanner.data.repository.TravelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedTripsViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {

    val savedTrips: StateFlow<List<TripEntity>> = travelRepository
        .getSavedTrips()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val savedDestinations: StateFlow<List<DestinationEntity>> = travelRepository
        .getSavedDestinations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteTrip(trip: TripEntity) {
        viewModelScope.launch { travelRepository.deleteTrip(trip) }
    }

    fun deleteDestination(destination: DestinationEntity) {
        viewModelScope.launch { travelRepository.deleteDestination(destination) }
    }
}
