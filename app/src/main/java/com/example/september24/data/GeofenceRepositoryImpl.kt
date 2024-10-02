package com.example.september24.data

import com.example.september24.domain.GeofenceRepository
import com.example.september24.data.dao.GeofenceDao
import com.example.september24.data.mappers.GeofenceMappers.toGeofenceEntity
import com.example.september24.data.models.GeofenceEntity
import com.google.android.gms.location.Geofence
import javax.inject.Inject

class GeofenceRepositoryImpl @Inject constructor(
    private val geofenceDao: GeofenceDao
) : GeofenceRepository {

    override suspend fun addGeofence(geofence: Geofence,reminderId: Int) {
        geofenceDao.insertGeofence(geofence.toGeofenceEntity(reminderId))
    }

    override suspend fun getGeofenceForReminder(reminderId: Int): GeofenceEntity? {
        return geofenceDao.getGeofenceForReminder(reminderId)
    }

    override suspend fun deleteGeofence(geofence: GeofenceEntity) {
        geofenceDao.deleteGeofence(geofence)
    }
}
