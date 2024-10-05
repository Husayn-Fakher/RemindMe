package com.example.september24.presentation

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.september24.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {
    fun sendNotification(reminderTitle: String, reminderText: String) {
        val notification = NotificationCompat.Builder(context, "geofence_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(reminderTitle)
            .setContentText(reminderText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)  // Notify using a notification ID
    }
}