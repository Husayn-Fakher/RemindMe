package com.example.september24.data.mappers

import com.example.september24.data.models.GeofenceEntity
import com.google.android.gms.location.Geofence

object GeofenceMappers {

    // Geofence to GeofenceEntity Mapper
    fun Geofence.toGeofenceEntity(reminderId: Int): GeofenceEntity {
        return GeofenceEntity(
            reminderId = reminderId,
            latitude = this.latitude,
            longitude = this.longitude,
            radius = this.radius,
            expirationDuration = this.expirationTime,
            transitionType = this.transitionTypes
        )
    }

    // GeofenceEntity to Geofence Mapper
    fun GeofenceEntity.toGeofence(): Geofence {
        return Geofence.Builder()
            .setRequestId(this.id.toString())
            .setCircularRegion(
                this.latitude,
                this.longitude,
                this.radius
            )
            .setExpirationDuration(this.expirationDuration)
            .setTransitionTypes(this.transitionType)
            .build()
    }
}