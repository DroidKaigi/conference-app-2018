package io.github.droidkaigi.confsched2018.presentation.staff.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Contributor
import io.github.droidkaigi.confsched2018.model.Staff
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorHeaderItem
import io.github.droidkaigi.confsched2018.presentation.contributor.item.ContributorItem

class StaffSection : Section() {
    fun updateStaff(topics: List<Staff>) {
        val header = StaffHeaderItem(topics.size)
        val list = mutableListOf<Item<*>>(header)
        topics.sortedByDescending { it.name }.mapTo(list) {
            StaffItem(it)
        }
        update(list)
    }
}
