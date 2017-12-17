package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent
import java.util.*

class DateSessionsGroup(val dataBindingComponent: FragmentDataBindingComponent) : UpdatingGroup() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session) -> Unit = {}
    ) {
        val sessionItems = sessions.map {
            SessionItem(it, onFavoriteClickListener, dataBindingComponent)
        }

        val dateSessionItemsMap: SortedMap<ReadableDateTimePair, List<SessionItem>>
                = sessionItems.groupBy {
            ReadableDateTimePair(it.session.startTime.toReadableDateString(),
                    it.session.startTime.toReadableTimeString())
        }.toSortedMap()

        val dateSessions = arrayListOf<Item<*>>()
        dateSessionItemsMap.keys.forEach { key ->
            key ?: return@forEach
            val list = dateSessionItemsMap[key]

            dateSessions.add(DateHeaderItem(key))
            dateSessions.addAll(list?.toMutableList().orEmpty())
        }
        update(dateSessions)
    }

    fun getDateFromPositionOrNull(firstItemPosition: Int): Date? {
        if (firstItemPosition < 0) return null

        var item = getItemOrNull(firstItemPosition) ?: return null
        item = item as? SessionItem ?: getItemOrNull(firstItemPosition + 1) ?: return null
        item as? SessionItem ?: return null

        return item.session.startTime
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }
}
