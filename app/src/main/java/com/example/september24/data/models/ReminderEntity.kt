package com.example.september24.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: Date,
    val time: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val textNote: String? = null

)