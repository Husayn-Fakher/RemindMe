package com.example.september24.domain.usecases

import com.example.september24.data.models.GeofenceEntity
import com.example.september24.domain.GeofenceRepository
import javax.inject.Inject

class GetGeofenceForReminderUseCase @Inject constructor(
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(reminderId: Int): GeofenceEntity? {
        return geofenceRepository.getGeofenceForReminder(reminderId)
    }
}