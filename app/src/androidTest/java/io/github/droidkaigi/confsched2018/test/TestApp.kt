package io.github.droidkaigi.confsched2018.test

import io.github.droidkaigi.confsched2018.presentation.App
import io.github.droidkaigi.confsched2018.test.di.TestAppInjector

class TestApp : App() {

    override fun setupDagger() {
        TestAppInjector.init(this)
    }

}
