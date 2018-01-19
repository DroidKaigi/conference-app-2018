package io.github.droidkaigi.confsched2018.presentation

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.tomoima.debot.DebotConfigurator
import timber.log.Timber

class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupLeakCanary()
        setupStetho()
        setupDebot()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setupLeakCanary() {
        LeakCanary.install(this)
    }

    private fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun setupDebot() {
        DebotConfigurator.configureWithDefault()
        registerActivityLifecycleCallbacks(DebotObserver())
    }
}
