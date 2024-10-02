package com.example.september24.di

import android.content.Context
import com.example.september24.data.GeofenceRepositoryImpl
import com.example.september24.data.ReminderRepositoryImpl
import com.example.september24.data.dao.GeofenceDao
import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.database.AppDatabase
import com.example.september24.domain.GeofenceRepository
import com.example.september24.domain.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)  // Instantiating the database here
    }

    @Provides
    @Singleton
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    fun provideGeofenceDao(appDatabase: AppDatabase): GeofenceDao {
        return appDatabase.geofenceDao()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(dao: ReminderDao): ReminderRepository {
        return ReminderRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideGeofenceRepository(dao: GeofenceDao): GeofenceRepository {
        return GeofenceRepositoryImpl(dao)
    }
}