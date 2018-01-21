package io.github.droidkaigi.confsched2018.presentation.common.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.droidkaigi.confsched2018.util.initNotificationChannel
import timber.log.Timber

class LocaleChangedBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("LocaleChangedBroadcastReceiver.onReceive")
        if (Intent.ACTION_LOCALE_CHANGED == intent!!.action) {
            initNotificationChannel(context!!)
        }
    }
}
