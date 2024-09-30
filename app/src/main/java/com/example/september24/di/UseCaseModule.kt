package com.example.september24.di

import com.example.september24.domain.DeleteReminderUseCase
import com.example.september24.domain.GetRemindersUseCase
import com.example.september24.domain.InsertReminderUseCase
import com.example.september24.domain.ReminderRepository
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
}
