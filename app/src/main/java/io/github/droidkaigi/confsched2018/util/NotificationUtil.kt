package io.github.droidkaigi.confsched2018.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.util.NotificationUtil.ChannelType

class NotificationUtil {
    enum class ChannelType(
            val id: String,
            @StringRes val nameRes: Int,
            val importance: Int
    ) {
        FAVORITE_SESSION_START(
                "favorite_session_start_channel",
                R.string.notification_channel_name_start_favorite_session,
                NotificationManager.IMPORTANCE_HIGH);
    }
}

fun notificationBuilder(context: Context, channelType: ChannelType): NotificationCompat.Builder {
    return NotificationCompat.Builder(context, channelType.id)
}

fun initNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context, ChannelType.FAVORITE_SESSION_START)
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
