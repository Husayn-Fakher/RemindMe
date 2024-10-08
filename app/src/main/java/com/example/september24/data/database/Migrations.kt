package com.example.september24.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add the new column 'textNote' to the 'reminders' table
            database.execSQL("ALTER TABLE reminders ADD COLUMN textNote TEXT")
        }
    }

