package com.example.september24.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.september24.domain.DeleteReminderUseCase
import com.example.september24.domain.GetRemindersUseCase
import com.example.september24.domain.InsertReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.september24.domain.models.Reminder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val insertReminderUseCase: InsertReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // A Flow that represents the list of reminders
    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    // Add error state for insert and delete operations
    private val _insertError = MutableStateFlow<String?>(null)
    val insertError: StateFlow<String?> = _insertError

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError

    private val _fetchError = MutableStateFlow<String?>(null)
    val fetchError: StateFlow<String?> = _fetchError

    var selectedLocation: LatLng? by mutableStateOf(null)


    init {
        getReminders()
    }
    // Use case to fetch all reminders
    fun getReminders() {
        viewModelScope.launch {
            getRemindersUseCase().collect { reminderList ->
                _reminders.value = reminderList
            }
        }
    }

    fun insertReminder(reminder: Reminder) {
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    insertReminderUseCase(reminder) // Use the use case instead of repository directly
                } catch (e: Exception) {
                    // Handle errors
                }
            }
        }
    }

    // Use case to delete a reminder
    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                deleteReminderUseCase(reminder)
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
}