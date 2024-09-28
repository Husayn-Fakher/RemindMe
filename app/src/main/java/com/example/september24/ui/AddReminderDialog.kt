package com.example.september24.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import com.example.september24.BuildConfig
import com.example.september24.R
import com.example.september24.data.model.Reminder
import com.example.september24.presentation.ReminderViewModel
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
    onAddReminder: (Reminder) -> Unit,
    autocompleteRequestCode: Int
) {

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // Consider using a Date Picker
    var time by remember { mutableStateOf("") } // Consider using a Time Picker
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // For selected location
    var showLocationPicker by remember { mutableStateOf(false) } // State to show location picker
    var showDatePicker by remember { mutableStateOf(false) } // State to show date picker
    var showTimePicker by remember { mutableStateOf(false) } // State to show time picker

    val context = LocalContext.current // Get the context

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                // Date picker trigger button
                Button(onClick = { showDatePicker = true }) {
                    Text(text = if (date.isEmpty()) "Select Date" else "Selected Date: $date")
                }

                // Time picker trigger button
                Button(onClick = { showTimePicker = true }) {
                    Text(text = if (time.isEmpty()) "Select Time" else "Selected Time: $time")
                }
                // Button to trigger location picker
                Button(onClick = { showLocationPicker = true }) {
                    Text("Select Location from Map")
                }
                // Button to trigger location picker
                Button (onClick = {
                    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(context as Activity)
                    (context as Activity).startActivityForResult(intent, autocompleteRequestCode)
                })  {
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
                        // Convert date string to Date object
                        val parsedDate = parseDate(date) // Implement parseDate function
                        if (parsedDate != null) {
                            // Check if the selected location is available
                            if (selectedLocation != null) {
                                // Create a reminder with title, date, time, and location coordinates
                                onAddReminder(Reminder(
                                    title = title,
                                    date = parsedDate,
                                    time = time,
                                    latitude = selectedLocation!!.latitude,  // Add latitude
                                    longitude = selectedLocation!!.longitude // Add longitude
                                ))
                            } else {
                                // Handle the case when location is not selected (optional)
                                Toast.makeText(context, "Please select a location.", Toast.LENGTH_SHORT).show()
                            }
                            onDismiss()
                        } else {
                            Toast.makeText(context, "Please select a date.", Toast.LENGTH_SHORT).show()
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