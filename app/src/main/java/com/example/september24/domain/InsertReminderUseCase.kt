package com.example.september24.domain

import com.example.september24.domain.models.Reminder

class InsertReminderUseCase(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) {
        reminderRepository.insertReminder(reminder)
    }
}