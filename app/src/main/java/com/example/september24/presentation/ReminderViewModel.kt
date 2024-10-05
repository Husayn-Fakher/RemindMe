package com.example.september24.presentation

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.september24.data.models.GeofenceEntity
import com.example.september24.data.receivers.GeofenceBroadcastReceiver
import com.example.september24.domain.usecases.DeleteReminderUseCase
import com.example.september24.domain.usecases.GetRemindersUseCase
import com.example.september24.domain.usecases.InsertReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.september24.domain.models.Reminder
import com.example.september24.domain.usecases.AddGeofenceUseCase
import com.example.september24.domain.usecases.DeleteGeofenceUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val insertReminderUseCase: InsertReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val addGeofenceUseCase: AddGeofenceUseCase,
    private val deleteGeofenceUseCase: DeleteGeofenceUseCase,
    private val notificationSender: NotificationSender,
    private val geofencePendingIntent: PendingIntent,
    private val geofencingClient: GeofencingClient
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

    // Use notificationSender in your logic, for example when a new reminder is added
    fun sendReminderNotification(title: String,text: String) {
        notificationSender.sendNotification(title ,text)
    }

    // Use case to add a geofence
    @SuppressLint("MissingPermission")
    fun addGeofence(geofence: Geofence, reminderId: Int) {

        viewModelScope.launch {
            try {
                // Create the GeofencingRequest
                val geofencingRequest = GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_DWELL)
                    .addGeofence(geofence)
                    .build()

                // Add the geofence to the client
                val result = geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                result.addOnSuccessListener {
                    Log.d("Geofence App", "Geofence added to client: ${geofence.requestId}")
                }
                result.addOnFailureListener { e ->
                    Log.e("Geofence App", "Failed to add geofence to the client: ${e.message}")
                }
                _geofenceError.value = null // Clear any previous error
            } catch (e: Exception) {
                _geofenceError.value = e.message // Set the error message
                Log.e("Geofence App", "Error adding geofence to the client: ${e.message}")
            }
            try {
                addGeofenceUseCase(geofence, reminderId)
                Log.d("Geofence App", "Geofence added: ${geofence.requestId}")
                _geofenceError.value = null // Clear any previous error
            } catch (e: Exception) {
                _geofenceError.value = e.message // Set the error message
                Log.e("Geofence App", "Error adding geofence: ${e.message}")
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