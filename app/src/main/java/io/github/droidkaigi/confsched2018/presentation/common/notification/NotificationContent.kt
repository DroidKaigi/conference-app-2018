package io.github.droidkaigi.confsched2018.presentation.common.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.detail.SessionDetailActivity

sealed class NotificationContent(
        open val title: String,
        open val text: String,
        val channelType: NotificationChannelType
) {
    enum class NotificationType {
        FAVORITE_SESSION_START_NOTIFICATION
    }

    data class FavoriteSessionStart(
            override val title: String,
            override val text: String,
            val sessionId: String
    ) : NotificationContent(
            title,
            text,
            NotificationChannelType.FAVORITE_SESSION_START
    ) {
        override fun createPendingContentIntent(context: Context): PendingIntent {
            return TaskStackBuilder
                    .create(context)
                    .addNextIntent(MainActivity.createIntent(context))
                    .addNextIntent(SessionDetailActivity.createIntent(context, sessionId))
                    .getPendingIntent(
                            sessionId.hashCode(),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ) as PendingIntent
        }

        override fun putExtrasTo(intent: Intent) {
            intent.apply {
                putExtra(
                        EXTRA_NOTIFICATION_ID,
                        NotificationType.FAVORITE_SESSION_START_NOTIFICATION.ordinal
                )
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_TEXT, text)
            }
        }

        companion object {
            fun parse(intent: Intent): FavoriteSessionStart {
                return FavoriteSessionStart(
                        intent.getStringExtra(EXTRA_TITLE),
                        intent.getStringExtra(EXTRA_TEXT),
                        intent.getStringExtra(EXTRA_SESSION_ID)
                )
            }
        }
    }

    abstract fun putExtrasTo(intent: Intent)
    abstract fun createPendingContentIntent(context: Context): PendingIntent

    companion object {
        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        private const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_TEXT = "EXTRA_TEXT"

        fun parse(intent: Intent): NotificationContent {
            return when (intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)) {
                NotificationType.FAVORITE_SESSION_START_NOTIFICATION.ordinal -> {
                    FavoriteSessionStart.parse(intent)
                }
                else -> throw NotImplementedError()
            }
        }
    }
}
