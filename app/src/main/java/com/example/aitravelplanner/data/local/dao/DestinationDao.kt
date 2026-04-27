package com.example.aitravelplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aitravelplanner.data.local.entity.DestinationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestination(destination: DestinationEntity)

    @Query("SELECT * FROM saved_destinations ORDER BY savedAt DESC")
    fun getAllDestinations(): Flow<List<DestinationEntity>>

    @Delete
    suspend fun deleteDestination(destination: DestinationEntity)

    @Query("DELETE FROM saved_destinations")
    suspend fun deleteAllDestinations()
}
