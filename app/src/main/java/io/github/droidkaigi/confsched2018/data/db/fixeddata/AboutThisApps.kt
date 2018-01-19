package io.github.droidkaigi.confsched2018.data.db.fixeddata

import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.AboutThisApp

class AboutThisApps {
    companion object {

        fun getThisApps(): List<AboutThisApp> {
            var index = 0
            return listOf(
                    // Official site
                    AboutThisApp(
                            10000 + index++,
                            R.string.about_official_site_title,
                            R.string.about_official_site_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Licenses
                    AboutThisApp(
                            10000 + index++,
                            R.string.about_licenses_title,
                            R.string.about_licenses_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Sponsors
                    AboutThisApp(
                            10000 + index++,
                            R.string.about_sponsors_title,
                            R.string.about_sponsors_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Contributors
                    AboutThisApp(
                            10000 + index++,
                            R.string.about_contributors_title,
                            R.string.about_contributors_description,
                            // TODO add navigation url
                            ""
                    ),
                    // Dev Version
                    AboutThisApp(
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
