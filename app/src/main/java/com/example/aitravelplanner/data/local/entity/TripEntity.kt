package com.example.aitravelplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val departureCity: String,
    val destination: String,
    val budgetUsd: Int,
    val days: Int,
    val travelStyle: String,
    val itinerary: String,
    val savedAt: Long = System.currentTimeMillis()
)
