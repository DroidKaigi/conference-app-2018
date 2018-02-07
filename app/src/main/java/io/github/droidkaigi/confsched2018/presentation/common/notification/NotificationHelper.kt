package io.github.droidkaigi.confsched2018.presentation.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import io.github.droidkaigi.confsched2018.R

enum class NotificationChannelType(
        val id: String,
        @StringRes val nameRes: Int,
        val importance: Int
) {
    FAVORITE_SESSION_START(
            "favorite_session_start_channel",
            R.string.notification_channel_name_start_favorite_session,
            NotificationManager.IMPORTANCE_HIGH
    ),
    NEW_FEED_POST(
            "new_feed_post",
            R.string.notification_channel_name_new_feed_post,
            NotificationManager.IMPORTANCE_HIGH
    );
}

fun Context.initNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannelType.values().forEach(::createNotificationChannel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Context.createNotificationChannel(channelType: NotificationChannelType) {
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(NotificationChannel(
                    channelType.id,
                    getString(channelType.nameRes),
                    channelType.importance
            ))
}

fun Context.showNotification(
        content: NotificationContent
) {
    NotificationManagerCompat.from(this).notify(
            content.text.hashCode(),
            NotificationCompat.Builder(this, content.channelType.id)
                    .setStyle(
                            NotificationCompat.BigTextStyle()
                                    .setBigContentTitle(content.title)
                                    .bigText(content.text)
                    )
                    .setFullScreenIntent(content.createPendingContentIntent(this), true)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.primary))
                    .setSmallIcon(R.drawable.ic_notification).build()
    )
}
