package io.github.droidkaigi.confsched2018.presentation.staff.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Staff

class StaffSection : Section() {
    fun updateStaff(staffList: List<Staff>) {
        val header = StaffHeaderItem(staffList.size)
        val list = mutableListOf<Item<*>>(header)
        staffList
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name }))
                .mapTo(list) {
                    StaffItem(it)
                }
        update(list)
    }
}
