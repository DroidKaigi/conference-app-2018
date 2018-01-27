package io.github.droidkaigi.confsched2018.presentation.common.pref

import android.content.SharedPreferences
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class PrefsKeyChangedListener @Inject constructor() {
    val listener: Flowable<String> by lazy {
        Flowable.create<String>({ emitter ->
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                emitter.onNext(key)
            }
            emitter.setCancellable {
                Prefs.preferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
            Prefs.preferences.registerOnSharedPreferenceChangeListener(listener)
        }, BackpressureStrategy.LATEST)
    }
}
