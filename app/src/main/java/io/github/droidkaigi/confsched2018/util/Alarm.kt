package io.github.droidkaigi.confsched2018.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateTimeString
import io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver.NotificationBroadcastReceiver
import java.util.concurrent.TimeUnit


class Alarm(val context: Context, val session: Session.SpeechSession) {
    fun register() {
        var time = session.startTime.getTime().toInt() - NOTIFICATION_TIME_BEFORE_START_MILLS
//        time = System.currentTimeMillis() + 61000
        if (System.currentTimeMillis() < time) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(context, session))
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(context, session))
            }
        }
    }

    fun unregister() {
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
        val intent = NotificationBroadcastReceiver.createIntent(context, session.id, title, text)
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
