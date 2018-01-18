package io.github.droidkaigi.confsched2018.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import io.github.droidkaigi.confsched2018.R

class NotificationUtil {
    companion object {
        const val FAVORITE_SESSION_START_CHANNEL_ID = "favorite_session_start_channel"
    }
}

fun notificationBuilder(context: Context, channelId: String): NotificationCompat.Builder {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createDefaultNotificationChannel(context, channelId)
    }
    @Suppress("DEPRECATION")
    val builder = NotificationCompat.Builder(context)
    builder.setChannelId(channelId)
    return builder
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createDefaultNotificationChannel(context: Context, channelId: String) {
    val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val appName = context.getString(R.string.app_name)
    val importance =
            when (channelId) {
                NotificationUtil.FAVORITE_SESSION_START_CHANNEL_ID -> {
                    NotificationManager.IMPORTANCE_HIGH
                }
                else -> {
                    NotificationManager.IMPORTANCE_DEFAULT
                }
            }
    val channel = NotificationChannel(channelId, appName, importance)
    notificationManager.createNotificationChannel(channel)
}

