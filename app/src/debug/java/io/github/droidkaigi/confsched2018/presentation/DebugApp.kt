package io.github.droidkaigi.confsched2018.presentation

import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.FragmentActivity
import com.facebook.stetho.Stetho
import com.github.takahirom.hyperion.plugin.simpleitem.SimpleItem
import com.github.takahirom.hyperion.plugin.simpleitem.SimpleItemHyperionPlugin
import com.squareup.leakcanary.LeakCanary
import com.tomoima.debot.Debot
import com.tomoima.debot.DebotConfigurator
import com.tomoima.debot.DebotStrategyBuilder
import io.github.droidkaigi.confsched2018.R
import timber.log.Timber

class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupLeakCanary()
        setupStetho()
        setupDebot()
        setupHyperion()
    }

    private fun setupHyperion() {
        val item = SimpleItem.Builder("Depot menu")
                .image(R.drawable.ic_notification)
                .clickListener { view ->
                    fun unwrapFragmentActivity(context: Context): FragmentActivity? {
                        if (context is FragmentActivity) return context
                        val baseContext = (context as? ContextWrapper)?.baseContext ?: return null
                        return unwrapFragmentActivity(baseContext)
                    }

                    val activity: FragmentActivity? = unwrapFragmentActivity(view.context)
                            ?: return@clickListener
                    Debot.getInstance().showDebugMenu(activity)
                }.build()
        SimpleItemHyperionPlugin.addItem(item)
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
        val builder = DebotStrategyBuilder.Builder()
                .registerMenu("Send Notification", NotificationDebotStrategy())
                .build()

        DebotConfigurator.configureWithCustomizedMenu(builder.strategyList)
    }
}
