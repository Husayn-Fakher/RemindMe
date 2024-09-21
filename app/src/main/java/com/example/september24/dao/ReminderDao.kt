package com.example.september24.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.september24.model.Reminder

class ReminderDao {

    @Dao
    interface ReminderDao {
        @Insert
        suspend fun insert(reminder: Reminder)

        @Delete
        suspend fun delete(reminder: Reminder)

        @Query("SELECT * FROM reminders")
        suspend fun getAllReminders(): List<Reminder>
    }
}