package io.github.droidkaigi.confsched2018.presentation.contributor.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class ContributorsSection(
        private val dataBindingComponent: FragmentDataBindingComponent
) : Section() {
    fun updateContributors(topics: List<Contributor>) {
        val list = mutableListOf<Item<*>>()
        topics.sortedByDescending { it.contributions }.mapTo(list) {
            ContributorItem(it, dataBindingComponent)
        }
        update(list)
    }
}
