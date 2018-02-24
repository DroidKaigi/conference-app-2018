package io.github.droidkaigi.confsched2018.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import androidx.net.toUri
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.common.fragment.Findable
import io.github.droidkaigi.confsched2018.presentation.sessions.AllSessionsFragment
import io.github.droidkaigi.confsched2018.util.CustomTabsHelper
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: AppCompatActivity) {
    private val containerId: Int = R.id.content
    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    private fun replaceFragment(fragment: Fragment) {
        val transaction = fragmentManager
            .beginTransaction()
            .replace(containerId, fragment, (fragment as? Findable)?.tagForFinding)

        if (fragmentManager.isStateSaved) {
            transaction.commitAllowingStateLoss()
        } else {
            transaction.commit()
        }
    }

    fun navigateToSessions() {
        replaceFragment(AllSessionsFragment.newInstance())
    }

    fun navigateToMainActivity() {
        MainActivity.start(activity)
    }

    fun navigateToExternalBrowser(url: String) {
        val customTabsPackageName = CustomTabsHelper.getPackageNameToUse(activity)
        if (tryLaunchingSpecificApp(url, customTabsPackageName)) {
            return
        }

        val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.primary))
                .build()
                .apply {
                    val referrer = "android-app://${activity.packageName}".toUri()
                    intent.putExtra(Intent.EXTRA_REFERRER, referrer)
                }
        val webUri = url.toUri()
        if (tryUsingCustomTabs(customTabsPackageName, customTabsIntent, webUri)) {
            return
        }

        // Cannot use custom tabs.
        activity.startActivity(customTabsIntent.intent.setData(webUri))
    }

    private fun tryLaunchingSpecificApp(url: String, customTabsPackageName: String?): Boolean {
        val appUri = url.toUri().let {
            if (it.host.contains("facebook")) {
                (FACEBOOK_SCHEME + url).toUri()
            } else it
        }
        val appIntent = Intent(Intent.ACTION_VIEW, appUri)
        val intentResolveInfo = activity.packageManager.resolveActivity(
                appIntent,
                PackageManager.MATCH_DEFAULT_ONLY
        )

        intentResolveInfo?.activityInfo?.packageName?.let {
            if (customTabsPackageName != null && it != customTabsPackageName) {
                // Open specific app
                activity.startActivity(appIntent)
                return true
            }
        }
        return false
    }

    private fun tryUsingCustomTabs(customTabsPackageName: String?,
                                   customTabsIntent: CustomTabsIntent,
                                   webUri: Uri?): Boolean {
        customTabsPackageName?.let {
            customTabsIntent.intent.`package` = customTabsPackageName
            customTabsIntent.launchUrl(activity, webUri)
            return true
        }
        return false
    }

    fun navigateImplicitly(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.resolveActivity(activity.packageManager)?.let {
                activity.startActivity(intent)
            }
        }
    }

    companion object {
        private const val FACEBOOK_SCHEME = "fb://facewebmodal/f?href="
    }
}
