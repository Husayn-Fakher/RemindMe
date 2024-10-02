package com.example.september24.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.september24.data.models.GeofenceEntity
import com.example.september24.domain.usecases.DeleteReminderUseCase
import com.example.september24.domain.usecases.GetRemindersUseCase
import com.example.september24.domain.usecases.InsertReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.september24.domain.models.Reminder
import com.example.september24.domain.usecases.AddGeofenceUseCase
import com.example.september24.domain.usecases.DeleteGeofenceUseCase
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val insertReminderUseCase: InsertReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val addGeofenceUseCase: AddGeofenceUseCase,
    private val deleteGeofenceUseCase: DeleteGeofenceUseCase
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

    // Geofence-related error state
    private val _geofenceError = MutableStateFlow<String?>(null)
    val geofenceError: StateFlow<String?> = _geofenceError

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

    suspend fun insertReminder(reminder: Reminder): Long {
        return viewModelScope.async {
            try {
                insertReminderUseCase(reminder) // This should return the ID
            } catch (e: Exception) {
                Log.e("InsertReminder", "Error inserting reminder: ${e.message}")
                -1 // Return a default value to indicate failure
            }
        }.await() // Await the result of the async block
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

    // Use case to add a geofence
    fun addGeofence(geofence: Geofence,reminderId: Int) {
        viewModelScope.launch {
            try {
                addGeofenceUseCase(geofence, reminderId)
                Log.d("Geofence", "Geofence added: ${geofence.requestId}")
                _geofenceError.value = null // Clear any previous error
            } catch (e: Exception) {
                _geofenceError.value = e.message // Set the error message
                Log.e("Geofence", "Error adding geofence: ${e.message}")
            }
        }
    }

    // Use case to delete a geofence
    fun deleteGeofence(geofence: GeofenceEntity) {
        viewModelScope.launch {
            try {
                deleteGeofenceUseCase(geofence) // Assuming you pass the ID
                _geofenceError.value = null // Clear any previous error
            } catch (e: Exception) {
                _geofenceError.value = e.message // Set the error message
            }
        }
    }
}