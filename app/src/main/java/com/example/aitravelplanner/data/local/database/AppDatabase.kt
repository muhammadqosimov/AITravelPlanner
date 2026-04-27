package com.example.aitravelplanner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aitravelplanner.data.local.dao.DestinationDao
import com.example.aitravelplanner.data.local.dao.TripDao
import com.example.aitravelplanner.data.local.entity.DestinationEntity
import com.example.aitravelplanner.data.local.entity.TripEntity

@Database(
    entities = [TripEntity::class, DestinationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun destinationDao(): DestinationDao
}
