package com.example.september24.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.september24.data.models.ReminderEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {
    // Insert a ReminderEntity into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity): Long

    // Delete a ReminderEntity from the database
    @Delete
    suspend fun delete(reminder: ReminderEntity)

    // Query to get all reminders as ReminderEntity objects
    @Query("SELECT * FROM reminders")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    // Update the full reminder
    @Update
    suspend fun update(reminder: ReminderEntity)
}
