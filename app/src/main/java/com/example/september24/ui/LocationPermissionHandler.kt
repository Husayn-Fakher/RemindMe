package com.example.september24.ui

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    // State to keep track of whether we should request background permission
    var shouldRequestBackgroundPermission by remember { mutableStateOf(false) }

    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("LocationPermissionHandler", "Foreground permission granted: $isGranted")
        if (isGranted) {
            // If foreground permission is granted, set the flag to request background permission
            shouldRequestBackgroundPermission = true
            Log.d("LocationPermissionHandler", "Foreground permission granted, requesting background permission")
        } else {
            onPermissionDenied()
        }
    }

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("LocationPermissionHandler", "Background permission granted: $isGranted")
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    // Check permissions status and handle requests
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("LocationPermissionHandler", "Foreground location permission is already granted")

                // If foreground permission is granted, check for background permission
                if (ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted()
                } else {
                    // Set the flag to request background permission
                    shouldRequestBackgroundPermission = true
                    Log.d("LocationPermissionHandler", "Requesting background location permission")
                }
            }
            else -> {
                // Request foreground location permission
                Log.d("LocationPermissionHandler", "Requesting foreground location permission")
                foregroundLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Launch the background permission request if needed
    if (shouldRequestBackgroundPermission) {
        Log.d("LocationPermissionHandler", "Launching background permission request")
        backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        shouldRequestBackgroundPermission = false // Reset the flag after requesting
    }
}

