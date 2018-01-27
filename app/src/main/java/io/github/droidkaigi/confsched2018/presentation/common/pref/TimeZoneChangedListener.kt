package io.github.droidkaigi.confsched2018.presentation.common.pref

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class TimeZoneChangedListener @Inject constructor(context: Context) {
    val listener: Flowable<Intent> by lazy {
        Flowable.create<Intent>({ emitter ->
            val receiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    intent?.let {
                        if (it.action == Intent.ACTION_TIMEZONE_CHANGED) emitter.onNext(it)
                    }
                }
            }
            emitter.setCancellable { context.unregisterReceiver(receiver) }
            context.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
        }, BackpressureStrategy.LATEST)
    }
}
