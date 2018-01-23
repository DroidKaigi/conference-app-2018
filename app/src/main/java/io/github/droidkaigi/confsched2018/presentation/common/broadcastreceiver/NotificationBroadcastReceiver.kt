package io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.util.NotificationHelper
import timber.log.Timber

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("NotificationBroadcastReceiver.onReceive")
        if (!Prefs.enableNotification) {
            Timber.d("Do not show Notification")
            return
        }

        when (intent?.action) {
            ACTION_FAVORITE_SESSION_START -> {
                Timber.d("Show Notification")
                val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
                val title = intent.getStringExtra(EXTRA_TITLE)
                val text = intent.getStringExtra(EXTRA_TEXT)
                val pendingIntent = TaskStackBuilder
                        .create(context!!)
                        .addNextIntent(MainActivity.createIntent(context))
                        .addNextIntent(SessionDetailActivity.createIntent(context, sessionId))
                        .getPendingIntent(sessionId.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)
                NotificationHelper.showFavoriteSessionStart(context, title, text, pendingIntent)
            }
        }
    }

    companion object {
        private const val ACTION_FAVORITE_SESSION_START = "ACTION_FAVORITE_SESSION_START"
        private const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_TEXT = "EXTRA_TEXT"
        fun createIntent(
                context: Context,
                sessionId: String,
                title: String,
                text: String
        ): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                action = ACTION_FAVORITE_SESSION_START
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_TEXT, text)
            }
        }
    }
}
