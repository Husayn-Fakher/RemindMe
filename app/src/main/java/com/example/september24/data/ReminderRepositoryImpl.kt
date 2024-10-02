package com.example.september24.data

import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.mappers.toDomainModel
import com.example.september24.data.mappers.toEntityModel
import com.example.september24.domain.ReminderRepository
import com.example.september24.domain.models.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository  {
    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insert(reminder.toEntityModel())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.delete(reminder.toEntityModel()) // Accessing DAO function
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.map { it.toDomainModel() } // Convert each ReminderEntity to Reminder
        }
    }
}