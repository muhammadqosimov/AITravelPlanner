package com.example.aitravelplanner.data.repository

import com.example.aitravelplanner.data.local.dao.DestinationDao
import com.example.aitravelplanner.data.local.dao.TripDao
import com.example.aitravelplanner.data.local.entity.DestinationEntity
import com.example.aitravelplanner.data.local.entity.TripEntity
import com.example.aitravelplanner.data.model.DestinationRequest
import com.example.aitravelplanner.data.model.TravelPlanRequest
import com.example.aitravelplanner.data.remote.GeminiService
import com.example.aitravelplanner.util.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepository @Inject constructor(
    private val geminiService: GeminiService,
    private val tripDao: TripDao,
    private val destinationDao: DestinationDao
) {

    fun generateTravelPlan(request: TravelPlanRequest): Flow<UiState<String>> = flow {
        emit(UiState.Loading)
        try {
            val result = geminiService.generateTravelItinerary(request)
            emit(UiState.Success(result))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Failed to generate itinerary"))
        }
    }

    fun findDestinations(request: DestinationRequest): Flow<UiState<String>> = flow {
        emit(UiState.Loading)
        try {
            val result = geminiService.findDestinations(request)
            emit(UiState.Success(result))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Failed to find destinations"))
        }
    }

    suspend fun saveTrip(trip: TripEntity) = tripDao.insertTrip(trip)

    fun getSavedTrips(): Flow<List<TripEntity>> = tripDao.getAllTrips()

    suspend fun deleteTrip(trip: TripEntity) = tripDao.deleteTrip(trip)

    suspend fun deleteAllTrips() = tripDao.deleteAllTrips()

    suspend fun saveDestination(destination: DestinationEntity) =
        destinationDao.insertDestination(destination)

    fun getSavedDestinations(): Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    suspend fun deleteDestination(destination: DestinationEntity) =
        destinationDao.deleteDestination(destination)

    suspend fun deleteAllDestinations() = destinationDao.deleteAllDestinations()
}
