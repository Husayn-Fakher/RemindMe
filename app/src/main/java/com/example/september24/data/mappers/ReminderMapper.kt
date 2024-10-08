package com.example.september24.data.mappers

import com.example.september24.data.models.ReminderEntity
import com.example.september24.domain.models.Location
import com.example.september24.domain.models.Reminder



fun ReminderEntity.toDomainModel(): Reminder {
    return Reminder(
        id = id,
        title = title,
        date = date,
        time = time,
        location = if (latitude != null && longitude != null) {
            Location(latitude, longitude)
        } else {
            null
        },
        textNote = textNote // Add the textNote field
    )
}

fun Reminder.toEntityModel(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        date = date,
        time = time,
        latitude = location?.latitude,
        longitude = location?.longitude,
        textNote = textNote
    )
}
