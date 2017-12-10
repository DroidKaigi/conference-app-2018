package io.github.droidkaigi.confsched2018.presentation

import android.app.Activity
import android.app.Application
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.droidkaigi.confsched2018.di.AppInjector
import javax.inject.Inject

open class App : Application(), HasActivityInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
        setupDagger()
        setupStetho()
    }

    private fun setupStetho() {
        Stetho.initializeWithDefaults(this)
    }

    open fun setupLeakCanary() {
        // override
    }

    open fun setupDagger() {
        AppInjector.init(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingAndroidInjector
}
