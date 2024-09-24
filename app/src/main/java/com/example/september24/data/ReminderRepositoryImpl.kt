package com.example.september24.data

import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.model.Reminder
import com.example.september24.domain.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository  {
    override suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder) // Accessing DAO function
    }

    override  fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders() // Accessing DAO function
    }
}