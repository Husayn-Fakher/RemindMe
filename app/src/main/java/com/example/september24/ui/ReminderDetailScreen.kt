package com.example.september24.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ReminderDetailScreen(
    navController: NavController,
    title: String,
    time: String,
    formattedDate: String,
    latitude: Double?,
    longitude: Double?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Title: $title", style = MaterialTheme.typography.titleLarge)
        Text(text = "Time: $time", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Date: $formattedDate", style = MaterialTheme.typography.bodyMedium)

        val markerPosition = LatLng(latitude?.toDouble() ?: 0.0, longitude?.toDouble() ?: 0.0)


        // Create a CameraPositionState for managing camera movements
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(markerPosition ?: LatLng(37.7749, -122.4194), 15f) // Default location
        }

        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                // Add a marker at the location
                if (latitude != null && longitude != null) {
                    Marker(
                        state = MarkerState(position = markerPosition),
                        title = title,
                        snippet = "Reminder Date: $formattedDate"
                    )
                }
            }
        }
    }

    }
