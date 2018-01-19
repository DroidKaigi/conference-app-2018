package io.github.droidkaigi.confsched2018.data.db.fixeddata

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
                            ""
                    ),
                    // Sponsors
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_sponsors_title,
                            R.string.about_sponsors_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Contributors
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_contributors_title,
                            R.string.about_contributors_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Dev Version
                    AboutThisApp.Item(
                            10000 + index++,
                            R.string.about_version_title,
                            R.string.versionInfo,
                            // TODO add navigation url
                            ""
                    )
            )
        }
    }
}
