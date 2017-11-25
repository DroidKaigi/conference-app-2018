package io.github.droidkaigi.confsched2018.presentation

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class DebugApp : Application() {
    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)
    }
}
