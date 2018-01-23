package io.github.droidkaigi.confsched2018.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import io.github.droidkaigi.confsched2018.R

/**
 * Helper class for Notification.
 */
object NotificationHelper {
    private enum class ChannelType(
            val id: String,
            @StringRes val nameRes: Int,
            val importance: Int
    ) {
        FAVORITE_SESSION_START(
                "favorite_session_start_channel",
                R.string.notification_channel_name_start_favorite_session,
                NotificationManager.IMPORTANCE_HIGH);
    }

    fun initNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChannelType.values().forEach {
                createNotificationChannel(context, it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelType: ChannelType) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = context.getString(channelType.nameRes)
        val channel = NotificationChannel(channelType.id, channelName, channelType.importance)
        notificationManager.createNotificationChannel(channel)
    }

    fun showFavoriteSessionStart(
            context: Context,
            title: String,
            text: String,
            pendingIntent: PendingIntent?
    ) {
        val notification =
                NotificationCompat.Builder(context, ChannelType.FAVORITE_SESSION_START.id)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setFullScreenIntent(pendingIntent, true)
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(context, R.color.primary))
                        .setSmallIcon(R.drawable.ic_notification).build()
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManagerCompat.notify(title.hashCode(), notification)
    }
}
