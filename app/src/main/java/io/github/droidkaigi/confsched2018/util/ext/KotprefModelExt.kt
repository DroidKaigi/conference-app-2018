package io.github.droidkaigi.confsched2018.util.ext

import android.content.SharedPreferences
import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import com.chibatching.kotpref.KotprefModel
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlin.reflect.KProperty0

@CheckResult fun <T> KotprefModel.asFlowable(property: KProperty0<T>, @StringRes key: Int):
        Flowable<T> {
    return asFlowable(property, context.getString(key))
}

@CheckResult fun <T> KotprefModel.asFlowable(property: KProperty0<T>, key: String? = null):
        Flowable<T> {
    return Flowable.create<T>({ emitter ->
        val listenKey = key ?: property.name
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changeKey ->
            if (listenKey == changeKey) {
                emitter.onNext(property.get())
            }
        }
        emitter.setCancellable {
            Prefs.preferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
        Prefs.preferences.registerOnSharedPreferenceChangeListener(listener)
    }, BackpressureStrategy.LATEST)
}
