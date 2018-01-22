package io.github.droidkaigi.confsched2018

import com.chibatching.kotpref.Kotpref
import io.github.droidkaigi.confsched2018.presentation.App
import org.robolectric.RuntimeEnvironment

class TestApp : App() {

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(RuntimeEnvironment.application)
    }

    override fun isInUnitTests(): Boolean {
        return true
    }
}
