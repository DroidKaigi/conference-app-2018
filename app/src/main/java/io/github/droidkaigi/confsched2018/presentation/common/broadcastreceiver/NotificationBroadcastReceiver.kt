package io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.util.NotificationUtil
import timber.log.Timber

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("NotificationBroadcastReceiver.onReceive")
        val sessionId = intent!!.getStringExtra(EXTRA_SESSION_ID)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val text = intent.getStringExtra(EXTRA_TEXT)
        val pendingIntent = TaskStackBuilder
                .create(context!!)
                .addNextIntent(MainActivity.createIntent(context))
                .addNextIntent(SessionDetailActivity.createIntent(context, sessionId))
                .getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
        NotificationUtil.showNotification(context, title, text,
                pendingIntent)
    }

    companion object {
        private val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        private val EXTRA_TITLE = "EXTRA_TITLE"
        private val EXTRA_TEXT = "EXTRA_TEXT"
        const val FAVORITE_SESSION_START_CHANNEL_ID = "favorite_session_start_channel"
        fun createIntent(context: Context, sessionId: String, title: String, text: String): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_TEXT, text)
            }
        }
    }
}
