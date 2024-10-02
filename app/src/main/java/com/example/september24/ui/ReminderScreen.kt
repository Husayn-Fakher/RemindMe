package com.example.september24.ui


import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.september24.BuildConfig
import com.example.september24.domain.models.Reminder
import com.example.september24.presentation.ReminderViewModel
import com.google.android.gms.location.Geofence
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun ReminderScreen(
    navController: NavController, viewModel: ReminderViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }
    val coroutineScope = rememberCoroutineScope() // Get the coroutine scope

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

    // Scaffold to show Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
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
                            // Format the date to a string
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            val formattedDate = dateFormat.format(reminder.date)
                            ReminderItem(
                                reminder = reminder,
                                onClick = {
                                    navController.navigate(
                                        "reminderDetail/${reminder.title}/${reminder.time}/$formattedDate/${reminder.location?.latitude}/${reminder.location?.longitude}"
                                    )},
                                onDelete = { reminder ->
                                    reminderToDelete = reminder
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                } }

        // Add Reminder Button at the bottom
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            AddReminderDialog(
                onDismiss = { showDialog = false },
                onAddReminder = { reminder,geofence ->
                    coroutineScope.launch {
                        // Inserting the Reminder
                        val reminderId = viewModel.insertReminder(reminder)

                        // Validate reminderId
                        if (reminderId > 0) { // Check if the ID is valid
                            // Inserting the Geofence
                            viewModel.addGeofence(geofence, reminderId.toInt())
                        } else {
                            Log.e("Geofence", "Failed to insert reminder, reminderId is invalid")
                        }
                    }
                }
            )
        }
            if (showDeleteDialog && reminderToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteReminder(reminderToDelete!!)
                            showDeleteDialog = false
                        }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Reminder") },
                    text = { Text("Are you sure you want to delete this reminder?") }
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
}

@Composable
fun ReminderItem(reminder: Reminder,
                 onClick: (Reminder) -> Unit,
                 onDelete: (Reminder) -> Unit ){ // Add a click callback to handle the click event)

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reminder.date)

    val apiKey = BuildConfig.MAPS_API_KEY
    val mapUrl = if (reminder.location != null) {
        "https://maps.googleapis.com/maps/api/staticmap?center=${reminder.location?.latitude},${reminder.location?.longitude}&zoom=20&size=800x800&key=$apiKey"
    } else {
        "https://maps.googleapis.com/maps/api/staticmap?center=0,0&zoom=1&size=400x400&key=$apiKey"
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).clickable { onClick(reminder) } // Handle click event

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
            Text(
                text = "Delete",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable { onDelete(reminder) }
            )
        }
    }
}



