package io.github.droidkaigi.confsched2018.presentation

import com.squareup.leakcanary.LeakCanary

class DebugApp : App() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun setupLeakCanary() {
        LeakCanary.install(this)
    }
}
