package io.github.droidkaigi.confsched2018.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.FontRequestEmojiCompatConfig
import android.support.v4.provider.FontRequest
import android.support.v7.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.di.DaggerAppComponent
import io.github.droidkaigi.confsched2018.di.DatabaseModule
import io.github.droidkaigi.confsched2018.di.NetworkModule
import io.github.droidkaigi.confsched2018.presentation.common.notification.initNotificationChannel
import io.github.droidkaigi.confsched2018.service.push.processor.NewPostProcessor
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

@SuppressLint("Registered")
open class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        setupFirebase()
        setupVectorDrawable()
        setupThreeTenABP()
        setupCalligraphy()
        setupEmoji()
        setupNotification()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun setupFirebase() {
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            val fireStore = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                    // https://github.com/DroidKaigi/conference-app-2018/issues/277#issuecomment-360171780
                    .setPersistenceEnabled(false)
                    .build()
            fireStore.firestoreSettings = settings
            // push notification for new feed
            FirebaseMessaging.getInstance().subscribeToTopic(NewPostProcessor.TOPIC)
        }
    }

    private fun setupVectorDrawable() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun setupThreeTenABP() {
        if (!isInUnitTests()) {
            AndroidThreeTen.init(this)
        }
    }

    private fun setupCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFont(R.font.notosans_medium)
                .build())
    }

    private fun setupEmoji() {
        val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs)
        val config = FontRequestEmojiCompatConfig(applicationContext, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(object : EmojiCompat.InitCallback() {
                    override fun onInitialized() {
                        Timber.i("EmojiCompat initialized")
                    }

                    override fun onFailed(throwable: Throwable?) {
                        Timber.e(throwable, "EmojiCompat initialization failed")
                    }
                })
        EmojiCompat.init(config)
    }

    private fun setupNotification() {
        initNotificationChannel()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .application(this)
                .networkModule(NetworkModule.instance)
                .databaseModule(DatabaseModule.instance)
                .build()
    }

    protected open fun isInUnitTests() = false
}
