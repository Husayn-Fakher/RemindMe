package com.example.september24.data

import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.mappers.toEntityModel
import com.example.september24.domain.ReminderRepository
import com.example.september24.domain.models.Reminder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ReminderRepositoryTest {

    private lateinit var reminderDao: ReminderDao
    private lateinit var reminderRepository: ReminderRepository

    @Before
    fun setUp() {
        reminderDao = mock() // Mock the ReminderDao
        reminderRepository = ReminderRepositoryImpl(reminderDao) // Initialize the repository with the mocked DAO
    }

    @Test
    fun `test insert reminder`() = runBlocking {
        // Arrange
        val reminder = Reminder(id = 1, title = "Buy groceries", date = mock(), time = "12:00 PM")

        // Act
        reminderRepository.insertReminder(reminder)

        // Assert
        verify(reminderDao).insert(reminder.toEntityModel())
    }

    @Test
    fun `test delete reminder`() = runBlocking {
        // Arrange
        val reminder = Reminder(id = 1, title = "Buy groceries", date = mock(), time = "12:00 PM")

        // Act
        reminderRepository.deleteReminder(reminder)

        // Assert
        verify(reminderDao).delete(reminder.toEntityModel())
    }

    @Test
    fun `test get all reminders`(): Unit = runBlocking {
        // Arrange
        val reminders = listOf(
            Reminder(id = 1, title = "Buy groceries", date = mock(), time = "12:00 PM"),
            Reminder(id = 2, title = "Doctor appointment", date = mock(), time = "3:00 PM")
        )
        `when`(reminderDao.getAllReminders()).thenReturn(flow { emit(reminders.map { it.toEntityModel() }) }) // Mocking Flow

        // Act
        val result = reminderRepository.getAllReminders().first() // Collect the first emission

        // Assert
        assert(result == reminders)
        assert(result.size == reminders.size)
        assert(result.containsAll(reminders))
        verify(reminderDao).getAllReminders()
    }
}