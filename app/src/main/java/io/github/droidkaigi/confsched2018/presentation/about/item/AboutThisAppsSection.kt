package io.github.droidkaigi.confsched2018.presentation.contributor.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class AboutThisAppsSection(
        private val dataBindingComponent: FragmentDataBindingComponent
) : Section() {

    fun updateAboutThisApps(aboutThisApps: List<AboutThisApp>) {
        val header = AboutThisAppHeaderItem(dataBindingComponent)
        val list = mutableListOf<Item<*>>(header)
        aboutThisApps.mapTo(list) {
            AboutThisAppItem(it, dataBindingComponent)
        }
        update(list)
    }
}
