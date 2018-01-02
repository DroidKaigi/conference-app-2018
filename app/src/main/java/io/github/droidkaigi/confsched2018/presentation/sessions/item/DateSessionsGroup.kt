package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString
import java.util.SortedMap

class DateSessionsGroup(private val fragment: Fragment) : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session) -> Unit
    ) {
        val sessionItems = sessions.map {
            SessionItem(it, onFavoriteClickListener, fragment)
        }

        val dateSessionItemsMap: SortedMap<ReadableDateTimePair, List<SessionItem>> =
                sessionItems.groupBy {
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

    fun getDateNumberOrNull(position: Int): Int? {
        if (position < 0) return null

        var item = getItemOrNull(position) ?: return null
        item = item as? SessionItem ?: getItemOrNull(position + 1) ?: return null
        item as? SessionItem ?: return null

        return item.session.dayNumber
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }
}
