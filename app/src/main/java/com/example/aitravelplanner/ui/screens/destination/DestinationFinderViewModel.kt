package com.example.aitravelplanner.ui.screens.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.local.entity.DestinationEntity
import com.example.aitravelplanner.data.model.DestinationRequest
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
class DestinationFinderViewModel @Inject constructor(
    private val travelRepository: TravelRepository
) : ViewModel() {

    private val _destinationsState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val destinationsState: StateFlow<UiState<String>> = _destinationsState.asStateFlow()

    private val _saveState = MutableStateFlow(false)
    val saveState: StateFlow<Boolean> = _saveState.asStateFlow()

    fun findDestinations(request: DestinationRequest) {
        travelRepository.findDestinations(request)
            .onEach { state -> _destinationsState.value = state }
            .launchIn(viewModelScope)
    }

    fun surpriseMe(budget: Int) {
        findDestinations(
            DestinationRequest(
                budgetUsd = budget,
                surpriseMode = true
            )
        )
    }

    fun saveDestination(name: String, rawResponse: String) {
        viewModelScope.launch {
            travelRepository.saveDestination(
                DestinationEntity(
                    name = name,
                    country = "",
                    estimatedCost = "",
                    description = rawResponse.take(300),
                    matchReason = "",
                    bestTimeToVisit = "",
                    rawAiResponse = rawResponse
                )
            )
            _saveState.value = true
        }
    }

    fun resetSaveState() { _saveState.value = false }
    fun reset() { _destinationsState.value = UiState.Idle }
}
