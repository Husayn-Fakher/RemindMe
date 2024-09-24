package com.example.september24.domain

import com.example.september24.data.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun insert(reminder: Reminder)
    suspend fun delete(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>
}