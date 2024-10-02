package com.example.september24.domain.usecases

import com.example.september24.data.models.GeofenceEntity
import com.example.september24.domain.GeofenceRepository
import com.google.android.gms.location.Geofence
import javax.inject.Inject

class AddGeofenceUseCase @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(geofence: Geofence,reminderId: Int) {
        geofenceRepository.addGeofence(geofence, reminderId)
    }
}
