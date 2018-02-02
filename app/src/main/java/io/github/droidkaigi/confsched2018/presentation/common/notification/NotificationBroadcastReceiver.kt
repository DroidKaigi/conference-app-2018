package io.github.droidkaigi.confsched2018.presentation.common.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import timber.log.Timber

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("NotificationBroadcastReceiver.onReceive")

        if (!Prefs.enableNotification) {
            Timber.d("Do not show Notification")
            return
        }

        intent ?: return
        context.showNotification(NotificationContent.parse(intent))
    }

    companion object {
        fun createIntent(
                context: Context,
                notificationContent: NotificationContent
        ): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                notificationContent.putExtrasTo(this)
            }
        }
    }
}
