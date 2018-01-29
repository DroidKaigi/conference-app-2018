package io.github.droidkaigi.confsched2018.presentation.contributor.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Contributor

class ContributorsSection : Section() {
    fun updateContributors(topics: List<Contributor>) {
        val header = ContributorHeaderItem(topics.size)
        val list = mutableListOf<Item<*>>(header)
        topics.sortedByDescending { it.contributions }.mapTo(list) {
            ContributorItem(it)
        }
        update(list)
    }
}
