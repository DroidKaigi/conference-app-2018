package io.github.droidkaigi.confsched2018.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.droidkaigi.confsched2018.di.AppInjector
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("Registered")
open class App : MultiDexApplication(), HasActivityInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupFirebase()
        setupVectorDrawable()
        setupThreeTenABP()
        setupLeakCanary()
        setupDagger()
        setupStetho()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setupFirebase() {
        if (!FirebaseApp.getApps(this).isEmpty()) {
            val fireStore = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
            fireStore.firestoreSettings = settings
        }
    }

    private fun setupVectorDrawable() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun setupThreeTenABP() {
        AndroidThreeTen.init(this)
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

    override fun activityInjector(): DispatchingAndroidInjector<Activity> =
            dispatchingAndroidInjector
}
