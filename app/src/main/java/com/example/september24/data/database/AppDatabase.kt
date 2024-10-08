package com.example.september24.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.september24.data.dao.GeofenceDao
import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.models.Converters
import com.example.september24.data.models.GeofenceEntity
import com.example.september24.data.models.ReminderEntity


@Database(entities = [ReminderEntity::class, GeofenceEntity::class], version = 2)
    @TypeConverters(Converters::class) // Add this line
    abstract class AppDatabase : RoomDatabase() {
        abstract fun reminderDao(): ReminderDao
        abstract fun geofenceDao(): GeofenceDao

        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "reminder_database"
                    ).addMigrations(MIGRATION_1_2).build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
