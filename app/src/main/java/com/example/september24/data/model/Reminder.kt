package com.example.september24.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: Date,
    val time: String,
    val latitude: Double? = null,  // Latitude of the location
    val longitude: Double? = null   // Longitude of the location
)
