package com.example.themadproject.model.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.core.app.NotificationCompat
import com.example.themadproject.MainActivity
import com.example.themadproject.R

class TrackerNotificationService(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val COUNTER_CHANNEL_ID = "tracker_channel"
    }

    fun showNotification(title: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context, 1, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.search_navigation)
            .setContentTitle(title)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}