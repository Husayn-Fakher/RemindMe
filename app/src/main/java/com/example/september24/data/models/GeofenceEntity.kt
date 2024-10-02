package com.example.september24.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "geofence_table",
    foreignKeys = [
        ForeignKey(
            entity = ReminderEntity::class,
            parentColumns = ["id"],
            childColumns = ["reminderId"],
            onDelete = ForeignKey.CASCADE // This specifies cascading delete
        )
    ]
)
data class GeofenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reminderId: Int,  // Link to the reminder
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val expirationDuration: Long,
    val transitionType: Int // Can represent ENTER, EXIT, etc.
)