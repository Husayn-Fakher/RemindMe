package com.example.september24.ui

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
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onAddReminder: (Reminder) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // Consider using a Date Picker
    var time by remember { mutableStateOf("") } // Consider using a Time Picker
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // For selected location
    var showLocationPicker by remember { mutableStateOf(false) } // State to show location picker
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
                TextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") } // Add Date Picker for better UX
                )
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") } // Add Time Picker for better UX
                )
                // Button to trigger location picker
                Button(onClick = { showLocationPicker = true }) {
                    Text("Select Location")
                }
                selectedLocation?.let {
                    Text("Selected Location: ${it.latitude}, ${it.longitude}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                        // Convert date string to Date object
                        val parsedDate = parseDate(date) // Implement parseDate function
                        if (parsedDate != null) {
                            onAddReminder(Reminder(title = title, date = parsedDate, time = time))

                            onDismiss()
                        } else {
                            val apiKey = BuildConfig.MAPS_API_KEY

                            Toast.makeText(context, "Invalid date format. Please use YYYY-MM-DD ", Toast.LENGTH_SHORT).show()
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
}

// Example function to parse a date string into a Date object
fun parseDate(dateString: String): Date? {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    } catch (e: Exception) {
        null // Return null if parsing fails
    }
}