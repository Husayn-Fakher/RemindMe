package com.example.september24.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.september24.data.GeofenceRepositoryImpl
import com.example.september24.data.ReminderRepositoryImpl
import com.example.september24.data.dao.GeofenceDao
import com.example.september24.data.dao.ReminderDao
import com.example.september24.data.database.AppDatabase
import com.example.september24.data.receivers.GeofenceBroadcastReceiver
import com.example.september24.domain.GeofenceRepository
import com.example.september24.domain.ReminderRepository
import com.example.september24.presentation.NotificationSender
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
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

    @Provides
    @Singleton
    fun provideNotificationSender(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): NotificationSender {
        return NotificationSender(context, notificationManager)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    // Provide the PendingIntent
    @Provides
    @Singleton
    fun provideGeofencePendingIntent(@ApplicationContext context: Context): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    // Provide the GeofencingClient
    @Provides
    @Singleton
    fun provideGeofencingClient(@ApplicationContext context: Context): GeofencingClient {
        return LocationServices.getGeofencingClient(context)
    }
}