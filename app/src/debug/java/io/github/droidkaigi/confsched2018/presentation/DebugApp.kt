package io.github.droidkaigi.confsched2018.presentation

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class DebugApp : App() {

    override fun setupLeakCanary() {
        LeakCanary.install(this)
    }

    override fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    override fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }
}
