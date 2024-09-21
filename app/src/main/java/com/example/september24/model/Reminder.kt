package com.example.september24.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: Date,
    val time: String
)
