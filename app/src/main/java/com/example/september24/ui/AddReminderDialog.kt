package com.example.september24.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.september24.data.models.GeofenceEntity
import com.example.september24.domain.models.Location
import com.example.september24.domain.models.Reminder
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onAddReminder: (Reminder,Geofence) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // Consider using a Date Picker
    var time by remember { mutableStateOf("") } // Consider using a Time Picker
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // For selected location
    var showLocationPicker by remember { mutableStateOf(false) } // State to show location picker
    var showDatePicker by remember { mutableStateOf(false) } // State to show date picker
    var showTimePicker by remember { mutableStateOf(false) } // State to show time picker

    val context = LocalContext.current // Get the context

    // Launcher to get result from AutocompleteActivity
    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                selectedLocation = place.location
                Toast.makeText(context, "Selected Location: ${place.displayName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Button(onClick = { showDatePicker = true }) {
                    Text(text = if (date.isEmpty()) "Select Date" else "Selected Date: $date")
                }
                Button(onClick = { showTimePicker = true }) {
                    Text(text = if (time.isEmpty()) "Select Time" else "Selected Time: $time")
                }
                Button(onClick = { showLocationPicker = true }) {
                    Text("Select Location from Map")
                }
                Button(onClick =
                    {
                        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(context)
                        autocompleteLauncher.launch(intent)

                }) {
                    Text("Type in Address")
                }

                selectedLocation?.let {
                    Text("Selected Location: ${it.latitude}, ${it.longitude}")
                }
            }
        },
        confirmButton = onClick@{
            Button(
                onClick = {
                    if (title.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                        val parsedDate = parseDate(date)
                        if (parsedDate != null && selectedLocation != null) {
                            val reminder = Reminder(
                                title = title,
                                date = parsedDate,
                                time = time,
                                location = Location(selectedLocation!!.latitude, selectedLocation!!.longitude)
                            )



                            // Create a geofence with a radius of 10 meters
                            val geofence = Geofence.Builder()
                                .setRequestId(reminder.title) // Use the reminder title as the geofence ID
                                .setCircularRegion(
                                    selectedLocation!!.latitude,
                                    selectedLocation!!.longitude,
                                    100f // Geofence radius in meters
                                )
                                .setExpirationDuration(Geofence.NEVER_EXPIRE) // Optional: set geofence expiration
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
                                .setLoiteringDelay(1000)
                                .build()

                            onAddReminder(reminder,geofence)
                            
                            onDismiss()
                        } else {
                            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Show the location picker dialog if the state is true
    if (showLocationPicker) {
        LocationPickerDialog(
            onDismiss = { showLocationPicker = false },
            onLocationPicked = { location ->
                selectedLocation = location
                showLocationPicker = false
            }
        )
    }

    // DatePicker Dialog
    if (showDatePicker) {
        val currentDate = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Update the date state with the selected date
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                date = selectedDate
                showDatePicker = false
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // TimePicker Dialog
    if (showTimePicker) {
        val currentTime = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // Update time state with selected time
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                time = selectedTime
                showTimePicker = false
            },
            currentTime.get(Calendar.HOUR_OF_DAY),
            currentTime.get(Calendar.MINUTE),
            true // Set to 'true' for 24-hour format, false for AM/PM format
        ).show()
    }
}



// Example function to parse a date string into a Date object
fun parseDate(dateString: String): Date? {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    } catch (e: Exception) {
        null // Return null if parsing fails
    }
}