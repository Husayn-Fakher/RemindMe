package com.example.september24.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // Leave space for the button
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

        // Add Reminder Button at the bottom
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

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align button at the bottom center
                .padding(16.dp) // Optional padding for the button
        ) {
            Text("Add Reminder")
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reminder.date)

    val apiKey = BuildConfig.MAPS_API_KEY
    val mapUrl = if (reminder.latitude != null && reminder.longitude != null) {
        "https://maps.googleapis.com/maps/api/staticmap?center=${reminder.latitude},${reminder.longitude}&zoom=20&size=800x800&key=$apiKey"
    } else {
        "https://maps.googleapis.com/maps/api/staticmap?center=0,0&zoom=1&size=400x400&key=$apiKey"
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.titleSmall, // Use a larger title style
                color = MaterialTheme.colorScheme.primary // Adjust color
            )
            Text(
                text = reminder.time,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground // Adjust color
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground // Adjust color
            )

            // Map preview image
            AsyncImage(
                model = mapUrl,
                contentDescription = "Map Preview for ${reminder.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)), // Rounded corners
                contentScale = ContentScale.Crop
            )
        }
    }
}



