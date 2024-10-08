package com.example.september24.di

import com.example.september24.domain.GeofenceRepository
import com.example.september24.domain.usecases.DeleteReminderUseCase
import com.example.september24.domain.usecases.GetRemindersUseCase
import com.example.september24.domain.usecases.InsertReminderUseCase
import com.example.september24.domain.ReminderRepository
import com.example.september24.domain.usecases.AddGeofenceUseCase
import com.example.september24.domain.usecases.DeleteGeofenceUseCase
import com.example.september24.domain.usecases.GetGeofenceForReminderUseCase
import com.example.september24.domain.usecases.UpdateReminderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideInsertReminderUseCase(reminderRepository: ReminderRepository): InsertReminderUseCase {
        return InsertReminderUseCase(reminderRepository)
    }

    @Provides
    fun provideGetRemindersUseCase(reminderRepository: ReminderRepository): GetRemindersUseCase {
        return GetRemindersUseCase(reminderRepository)
    }

    @Provides
    fun provideDeleteReminderUseCase(reminderRepository: ReminderRepository): DeleteReminderUseCase {
        return DeleteReminderUseCase(reminderRepository)
    }

    @Provides
    fun provideUpdateReminderUseCase(reminderRepository: ReminderRepository): UpdateReminderUseCase {
        return UpdateReminderUseCase(reminderRepository)
    }

    // Geofence Use Cases
    @Provides
    fun provideAddGeofenceUseCase(geofenceRepository: GeofenceRepository): AddGeofenceUseCase {
        return AddGeofenceUseCase(geofenceRepository)
    }

    @Provides
    fun provideDeleteGeofenceUseCase(geofenceRepository: GeofenceRepository): DeleteGeofenceUseCase {
        return DeleteGeofenceUseCase(geofenceRepository)
    }

    @Provides
    fun provideGetGeofenceForReminderUseCase(geofenceRepository: GeofenceRepository): GetGeofenceForReminderUseCase {
        return GetGeofenceForReminderUseCase(geofenceRepository)
    }
}
