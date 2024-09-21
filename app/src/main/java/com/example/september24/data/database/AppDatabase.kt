package com.example.september24.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.model.Converters
import com.example.september24.data.model.Reminder



    @Database(entities = [Reminder::class], version = 1)
    @TypeConverters(Converters::class) // Add this line
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
