package com.example.september24.domain

import com.example.september24.data.models.GeofenceEntity
import com.google.android.gms.location.Geofence

interface GeofenceRepository {
    suspend fun addGeofence(geofence: Geofence,reminderId: Int)
    suspend fun getGeofenceForReminder(reminderId: Int): GeofenceEntity?
    suspend fun deleteGeofence(geofence: GeofenceEntity)
}