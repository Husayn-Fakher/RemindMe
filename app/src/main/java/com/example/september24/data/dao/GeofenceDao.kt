package com.example.september24.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.september24.data.models.GeofenceEntity

@Dao
interface GeofenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: GeofenceEntity)

    @Query("SELECT * FROM geofence_table WHERE reminderId = :reminderId")
    suspend fun getGeofenceForReminder(reminderId: Int): GeofenceEntity?

    @Delete
    suspend fun deleteGeofence(geofence: GeofenceEntity)
}
