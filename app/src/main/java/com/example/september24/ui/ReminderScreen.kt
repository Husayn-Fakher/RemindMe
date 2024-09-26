package com.example.september24.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.september24.data.model.Reminder
import com.example.september24.presentation.ReminderViewModel

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

    val context = LocalContext.current
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
    Text(text = reminder.title)
    Text(text = reminder.time)
    Text(text = reminder.date.toString())
}
