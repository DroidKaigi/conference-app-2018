package io.github.droidkaigi.confsched2018.presentation

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import javax.inject.Inject

/**
 * Copyright 2018 G-CREATE
 */
@SuppressLint("StaticFieldLeak")
class MainViewModel @Inject constructor(
        // this field is application context not activity context
        private val applicationContext: Context
) : ViewModel(), LifecycleObserver {

    val configChangeEvent: MutableLiveData<Boolean> = MutableLiveData()
    private val timezoneFilter = IntentFilter(Intent.ACTION_TIMEZONE_CHANGED)
    private val timezoneReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    Intent.ACTION_TIMEZONE_CHANGED -> {
                        onConfigChanged()
                    }
                    else -> Unit
                }
            }
        }
    }
    private val preferenceChangedListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                // if you want to send configuration change event to activity, add key in when
                // expression
                when (key) {
                    applicationContext.getString(R.string.pref_key_enable_local_time) ->
                        onConfigChanged()
                    applicationContext.getString(R.string.pref_key_enable_hide_bottom_navigation) ->
                        onConfigChanged()
                    else -> Unit
                }
            }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        applicationContext.registerReceiver(timezoneReceiver, timezoneFilter)
        Prefs.preferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        applicationContext.unregisterReceiver(timezoneReceiver)
        Prefs.preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    private fun onConfigChanged() {
        configChangeEvent.postValue(true)
    }
}
