package com.example.september24.domain.usecases

import com.example.september24.domain.ReminderRepository
import com.example.september24.domain.models.Reminder

class InsertReminderUseCase(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder): Long {
        return reminderRepository.insertReminder(reminder)
    }
}