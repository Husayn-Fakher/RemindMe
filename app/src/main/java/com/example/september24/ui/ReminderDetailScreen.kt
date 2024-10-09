package com.example.september24.ui

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.september24.domain.models.Reminder
import com.example.september24.presentation.ReminderViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale

@Composable
fun ReminderDetailScreen(
    navController: NavController,
    id: Long,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collect the state of the reminders from the ViewModel
    val reminders by viewModel.reminders.collectAsState()

    val reminder = reminders.find { it.id == id }
    var noteText by remember { mutableStateOf(reminder?.textNote ?: "") }


    // Update noteText whenever the reminder changes
    LaunchedEffect(reminder) {
        noteText = reminder?.textNote ?: ""
    }

    val title = reminder?.title
    val time = reminder?.time
    val formattedDate = formatReminderDate(reminder)
    val latitude = reminder?.location?.latitude
    val longitude = reminder?.location?.longitude

    var markerPosition by remember { mutableStateOf(LatLng(latitude ?: 0.0, longitude ?: 0.0)) }


    // Create a CameraPositionState for managing camera movements
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }

    // This effect will re-run when latitude or longitude changes, ensuring recomposition.
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            markerPosition = LatLng(latitude, longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Title: $title", style = MaterialTheme.typography.titleLarge)
        Text(text = "Time: $time", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Date: $formattedDate", style = MaterialTheme.typography.bodyMedium)

        // Google Map
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Set height for the map
        ) {
            if (latitude != null && longitude != null) {
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = reminder.title,
                    snippet = "Reminder Date: $formattedDate"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to open Google Maps
        Button(
            onClick = {
                val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($title)")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Open in Google Maps")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for writing notes
            TextField(
                value = noteText,
                onValueChange = { newText -> noteText = newText  },
                label = { Text(text = "Write a note") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 4,
                singleLine = false
            )
        // Button to save the note
        Button(
            onClick = {
                reminder?.let {
                    viewModel.updateReminderText(it, noteText) // Call the update function
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(text = "Save Note")
        }
    }
}
fun formatReminderDate(reminder: Reminder?): String? {
    return reminder?.date?.let { date ->
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        dateFormat.format(date)
    }}