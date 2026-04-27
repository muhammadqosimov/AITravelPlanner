package com.example.aitravelplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aitravelplanner.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Query("SELECT * FROM saved_trips ORDER BY savedAt DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM saved_trips")
    suspend fun deleteAllTrips()
}
