package com.example.september24.domain.usecases

import com.example.september24.data.models.GeofenceEntity
import com.example.september24.domain.GeofenceRepository
import javax.inject.Inject

class DeleteGeofenceUseCase @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(geofence: GeofenceEntity) {
        geofenceRepository.deleteGeofence(geofence)
    }
}
