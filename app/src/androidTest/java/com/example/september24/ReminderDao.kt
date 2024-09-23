package com.example.september24

import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.database.AppDatabase
import com.example.september24.data.model.Reminder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
class ReminderDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase

    private lateinit var reminderDao: ReminderDao

    @Before
    fun setup() {
        hiltRule.inject()
        reminderDao = database.reminderDao()
    }

    @After
    fun tearDown() {
        database.close()
    }




    @Test
    fun insertAndGetReminder() = runBlocking {
        val reminder = Reminder(title = "Test Reminder", date = Date(), time = "10:00 AM")
        reminderDao.insert(reminder)

        // Retrieve all reminders
        val reminders = reminderDao.getAllReminders()

        // Assert that the list is not empty
        assert(reminders.isNotEmpty())

        // Find the inserted reminder based on title and time
        val insertedReminder = reminders.find {
            it.title == reminder.title && it.time == reminder.time
        }

        // Assert that the inserted reminder is found
        assert(insertedReminder != null)
        assert(insertedReminder?.title == reminder.title)
        assert(insertedReminder?.time == reminder.time)
    }
}