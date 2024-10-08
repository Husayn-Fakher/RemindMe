package com.example.september24.domain

import com.example.september24.domain.models.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insertReminder(reminder: Reminder): Long
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>
}