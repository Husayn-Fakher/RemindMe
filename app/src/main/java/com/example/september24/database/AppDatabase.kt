package com.example.september24.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.september24.dao.ReminderDao
import com.example.september24.model.Reminder

class AppDatabase {

    @Database(entities = [Reminder::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun reminderDao(): ReminderDao

        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "reminder_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}