package com.di

import android.content.Context
import androidx.room.Room
import com.example.september24.data.ReminderRepositoryImpl
import com.example.september24.data.database.AppDatabase
import com.example.september24.di.AppModule
import com.example.september24.domain.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class] // Replace the production module
)
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(database: AppDatabase): ReminderRepository {
        return ReminderRepositoryImpl(database.reminderDao())
    }

}