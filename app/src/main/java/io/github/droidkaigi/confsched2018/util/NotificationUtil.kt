package io.github.droidkaigi.confsched2018.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver.NotificationBroadcastReceiver

class NotificationUtil {
    companion object {
        fun showNotification(
                context: Context,
                title: String,
                text: String,
                pendingIntent: PendingIntent?
        ) {
            val notification = getNotificationBuilder(context)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher).build()

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(NotificationBroadcastReceiver.FAVORITE_SESSION_START_CHANNEL_ID.hashCode(), notification)
        }

        fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createDefaultNotificationChannel(context)
            }
            @Suppress("DEPRECATION")
            val builder = NotificationCompat.Builder(context)
            builder.setChannelId(NotificationBroadcastReceiver.FAVORITE_SESSION_START_CHANNEL_ID)
            return builder
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createDefaultNotificationChannel(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val appName = context.getString(R.string.app_name)
            val channel = NotificationChannel(NotificationBroadcastReceiver.FAVORITE_SESSION_START_CHANNEL_ID,
                    appName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
