package com.institute.transport.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.institute.transport.R
import com.institute.transport.TransportApplication

/**
 * Firebase Cloud Messaging service for push notifications
 */
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        Log.d(TAG, "Message received from: ${message.from}")
        
        // Handle notification payload
        message.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Bus Alert",
                body = notification.body ?: ""
            )
        }
        
        // Handle data payload
        message.data.isNotEmpty().let {
            Log.d(TAG, "Message data: ${message.data}")
            handleDataMessage(message.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")
        // TODO: Send token to server if needed
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            "stop_arrival" -> {
                val stopName = data["stopName"] ?: "Unknown Stop"
                val routeName = data["routeName"] ?: "Unknown Route"
                showNotification(
                    title = "Bus Arrived!",
                    body = getString(R.string.bus_arrived_at_stop, stopName)
                )
            }
        }
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        
        val notification = NotificationCompat.Builder(
            this,
            TransportApplication.NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val TAG = "MessagingService"
    }
}
