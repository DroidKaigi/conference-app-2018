package io.github.droidkaigi.confsched2018.presentation

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.tomoima.debot.Debot

class DebotObserver : Application.ActivityLifecycleCallbacks {

    val debot: Debot by lazy {
        Debot.getInstance()
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity !is Activity) return
        debot.allowShake(activity.applicationContext)
    }

    override fun onActivityStarted(activity: Activity?) {
        // no-op
    }

    override fun onActivityResumed(activity: Activity?) {
        if (activity !is FragmentActivity) return
        debot.startSensor(activity)
    }

    override fun onActivityPaused(activity: Activity?) {
        debot.stopSensor()
    }

    override fun onActivityStopped(activity: Activity?) {
        // no-op
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        // no-op
    }

    override fun onActivityDestroyed(activity: Activity?) {
        // no-op
    }
}
