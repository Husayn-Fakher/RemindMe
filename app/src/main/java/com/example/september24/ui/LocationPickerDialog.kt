package com.example.september24.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationPickerDialog(
    onDismiss: () -> Unit,
    onLocationPicked: (LatLng) -> Unit
) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Location") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f) // Initial camera position
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        Log.d("LocationPicker", "Selected LatLng: $latLng") // Log clicked coordinates
                        selectedLocation = latLng
                    }
                ) {
                    selectedLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Selected Location",
                            snippet = "${it.latitude}, ${it.longitude}"
                        )
                    }
                }

                selectedLocation?.let {
                    Text(
                        text = "Selected Location: ${it.latitude}, ${it.longitude}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedLocation != null) {
                        onLocationPicked(selectedLocation!!)
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please select a location", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
