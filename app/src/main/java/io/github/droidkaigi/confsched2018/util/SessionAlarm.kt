package io.github.droidkaigi.confsched2018.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.common.notification.NotificationBroadcastReceiver
import io.github.droidkaigi.confsched2018.presentation.common.notification.NotificationContent
import io.github.droidkaigi.confsched2018.util.ext.toReadableDateTimeString
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionAlarm @Inject constructor(val context: Context) {
    /**
     * Toggle Session Alarm
     * this method use [session.isFavorited] for determinate register or unregister Alarm
     * If [session.isFavorited] is true, this method unregister. And else register
     */
    fun toggleRegister(session: Session.SpeechSession) {
        if (session.isFavorited) unregister(session) else register(session)
    }

    private fun register(session: Session.SpeechSession) {
        val time = session.startTime.time - NOTIFICATION_TIME_BEFORE_START_MILLS

        if (System.currentTimeMillis() < time) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time,
                        createAlarmIntent(context, session)
                )
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(context, session))
            }
        }
    }

    private fun unregister(session: Session.SpeechSession) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createAlarmIntent(context, session))
    }

    private fun createAlarmIntent(context: Context, session: Session.SpeechSession): PendingIntent {
        val title = context.getString(R.string.notification_title, session.title)
        val displaySTime = session.startTime.toReadableDateTimeString()
        val displayETime = session.endTime.toReadableDateTimeString()
        val text = context.getString(R.string.notification_message,
                displaySTime,
                displayETime,
                session.room.name)
        val notificationContent = NotificationContent.FavoriteSessionStart(title, text, session.id)
        val intent = NotificationBroadcastReceiver.createIntent(
                context,
                notificationContent
        )
        return PendingIntent.getBroadcast(
                context,
                session.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    companion object {
        private val NOTIFICATION_TIME_BEFORE_START_MILLS = TimeUnit.MINUTES.toMillis(10)
    }
}
