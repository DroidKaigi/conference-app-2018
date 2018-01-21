package io.github.droidkaigi.confsched2018.data.db.fixeddata

import android.net.Uri
import io.github.droidkaigi.confsched2018.BuildConfig
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.AboutThisApp

class AboutThisApps {
    companion object {

        fun getThisApps(): List<AboutThisApp> {
            var index = 0
            return listOf(
                    // Head Item
                    AboutThisApp.HeadItem(
                            10000 + index++,
                            R.string.about_official_head_title,
                            R.string.about_official_head_description,
                            "",
                            "https://www.facebook.com/DroidKaigi/",
                            "https://twitter.com/droidkaigi",
                            "https://github.com/DroidKaigi/conference-app-2018",
                            "https://www.youtube.com/channel/UCgK6L-PKx2OZBuhrQ6mmQZw"
                    ),
                    // Official site
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_official_site_title,
                            R.string.about_official_site_description,
                            "https://droidkaigi.jp/2018/"
                    ),
                    // Licenses
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_licenses_title,
                            R.string.about_licenses_description,
                            // TODO add navigation url
                            getUriBuilder("licenses")
                    ),
                    // Sponsors
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_sponsors_title,
                            R.string.about_sponsors_description,
                            getUriBuilder("sponsors")
                    ),
                    // Contributors
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_contributors_title,
                            R.string.about_contributors_description,
                            getUriBuilder("contributors")
                    ),
                    // Dev Version
                    AboutThisApp.Item(
                            10000 + index,
                            R.string.about_version_title,
                            R.string.versionInfo,
                            // TODO add navigation url
                            ""
                    )
            )
        }

        private fun getUriBuilder(path: String): String {
            val scheme = "conference"
            val host = "droidkaigi.co.jp" +
                    if (BuildConfig.DEBUG) "." + BuildConfig.BUILD_TYPE else ""
            return Uri.Builder()
                    .scheme(scheme)
                    .authority(host)
                    .path(path)
                    .build()
                    .toString()
        }
    }
}
