package io.github.droidkaigi.confsched2018.presentation.contributor.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class AboutThisAppsSection(
        private val dataBindingComponent: FragmentDataBindingComponent
) : Section() {

    fun updateAboutThisApps(
            aboutThisApps: List<AboutThisApp>,
            onAboutThisHeaderIconClickListener: (String) -> Unit?
    ) {
        val headItem = aboutThisApps.first { it is AboutThisApp.HeadItem } as AboutThisApp.HeadItem
        val header = AboutThisAppHeaderItem(
                headItem,
                onAboutThisHeaderIconClickListener,
                dataBindingComponent
        )
        val list = mutableListOf<Item<*>>(header)
        aboutThisApps.filter { it is AboutThisApp.Item }
                .mapTo(list) {
                    AboutThisAppItem(it, dataBindingComponent)
                }
        update(list)
    }
}
