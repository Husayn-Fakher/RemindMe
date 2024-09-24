package com.example.september24.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.september24.data.model.Reminder
import kotlinx.coroutines.flow.Flow


@Dao
    interface ReminderDao {
        @Insert
        suspend fun insert(reminder: Reminder)

        @Delete
        suspend fun delete(reminder: Reminder)

        @Query("SELECT * FROM reminders")
        fun getAllReminders(): Flow<List<Reminder>>
    }
