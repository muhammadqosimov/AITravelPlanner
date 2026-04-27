package com.example.aitravelplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_destinations")
data class DestinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val estimatedCost: String,
    val description: String,
    val matchReason: String,
    val bestTimeToVisit: String,
    val rawAiResponse: String,
    val savedAt: Long = System.currentTimeMillis()
)
