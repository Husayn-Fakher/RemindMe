package com.example.september24.data.receivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.september24.MyApplication
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // Get the NotificationSender instance
        val notificationSender = MyApplication.getNotificationSender()
        Toast.makeText(context, "GeofenceBroadcastReceiver has been triggered", Toast.LENGTH_SHORT).show()

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val transition = geofencingEvent?.geofenceTransition
        var transitionEvent = " did something unkown "

        val requestID = geofencingEvent?.triggeringGeofences?.get(0)?.requestId
        val size = geofencingEvent?.triggeringGeofences?.size


        when (transition) {

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                transitionEvent = "Exited"
                Log.d("Geofence App", "Exited geofence")
            }

            Geofence.GEOFENCE_TRANSITION_ENTER -> {

                transitionEvent = "Entered"
                Log.d("Geofence App", "Entered geofence")
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                transitionEvent = " are dwelling in "
                Log.d("Geofence App", "Dwelling in geofence")
            }
        }

        val notificationText = " You "+transitionEvent+" "+requestID+" "+size
        if (geofencingEvent != null) {
            notificationSender.sendNotification("geofencingEvent " , notificationText)
        }else{
            notificationSender.sendNotification("geofencingEvent ", "geofencingEvent is null")
        }
    }
}