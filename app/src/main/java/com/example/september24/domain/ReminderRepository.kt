package com.example.september24.domain

import com.example.september24.data.model.Reminder

interface ReminderRepository {
    suspend fun insert(reminder: Reminder)
    suspend fun delete(reminder: Reminder)
    suspend fun getAllReminders(): List<Reminder>
}