package io.github.droidkaigi.confsched2018.test

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.droidkaigi.confsched2018.di.DaggerAppComponent
import io.github.droidkaigi.confsched2018.presentation.App
import io.github.droidkaigi.confsched2018.test.di.StubDatabaseModule
import io.github.droidkaigi.confsched2018.test.di.StubNetworkModule

class TestApp : App() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .application(this)
                .networkModule(StubNetworkModule())
                .databaseModule(StubDatabaseModule())
                .build()
    }
}
