package io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity
import io.github.droidkaigi.confsched2018.util.notificationBuilder
import timber.log.Timber

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("NotificationBroadcastReceiver.onReceive")
        val sessionId = intent!!.getStringExtra(EXTRA_SESSION_ID)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val text = intent.getStringExtra(EXTRA_TEXT)
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID)
        val pendingIntent = TaskStackBuilder
                .create(context!!)
                .addNextIntent(MainActivity.createIntent(context))
                .addNextIntent(SessionDetailActivity.createIntent(context, sessionId))
                .getPendingIntent(sessionId.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)
        showNotification(context, title, text, pendingIntent, channelId)
    }

    private fun showNotification(
            context: Context,
            title: String,
            text: String,
            pendingIntent: PendingIntent?,
            channelId: String
    ) {
        val notification = notificationBuilder(context, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher).build()

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(title.hashCode(), notification)
    }

    companion object {
        private val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        private val EXTRA_TITLE = "EXTRA_TITLE"
        private val EXTRA_TEXT = "EXTRA_TEXT"
        private val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        fun createIntent(
                context: Context,
                sessionId: String,
                title: String,
                text: String,
                channelId: String
        ): Intent {
            return Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_TEXT, text)
                putExtra(EXTRA_CHANNEL_ID, channelId)
            }
        }
    }
}
