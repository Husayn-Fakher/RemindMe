package com.example.september24.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.september24.BuildConfig
import com.example.september24.data.model.Reminder
import com.example.september24.presentation.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel() // Using Hilt to inject the ViewModel
) {
    // Permission handling
    LocationPermissionHandler(
        onPermissionGranted = {

        },
        onPermissionDenied = {
            // Handle permission denial here (e.g., show a message)
        }
    )

    // Collect the state of the reminders from the ViewModel
    val reminders by viewModel.reminders.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Reminders",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (reminders.isEmpty()) {
            Text("No reminders available")
        } else {
            LazyColumn {
                items(reminders) { reminder ->
                    ReminderItem(reminder)
                }
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddReminderDialog(
            onDismiss = { showDialog = false },
            onAddReminder = { reminder ->
                // Call the ViewModel's insertReminder here
                viewModel.insertReminder(reminder)
            }
        )
    }

    Column {
        Button(onClick = { showDialog = true }) {
            Text("Add Reminder")
        }}

}

@Composable
fun ReminderItem(reminder: Reminder) {

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reminder.date)

    // Access the API key from BuildConfig (from the environment variable)
    val apiKey = BuildConfig.MAPS_API_KEY


    val mapUrl = if (reminder.latitude != null && reminder.longitude != null) {
        "https://maps.googleapis.com/maps/api/staticmap?center=${reminder.latitude},${reminder.longitude}&zoom=20&size=800x800&key=$apiKey"
    } else {
        "https://maps.googleapis.com/maps/api/staticmap?center=0,0&zoom=1&size=400x400&key=$apiKey"
    }

    Column {
        Text(text = reminder.title)
        Text(text = reminder.time)
        Text(text = formattedDate) // Display the formatted date
      //  Log.d("ReminderItem", "Map URL: $mapUrl")

        // Map preview image
        // Map preview image using AsyncImage from Coil
        AsyncImage(
            model = mapUrl, // Load the map image
            contentDescription = "Map Preview for ${reminder.title}",
            modifier = Modifier, // Customize modifier as needed
            contentScale = ContentScale.Crop // Optional, adjust scaling
        )
    }


}
