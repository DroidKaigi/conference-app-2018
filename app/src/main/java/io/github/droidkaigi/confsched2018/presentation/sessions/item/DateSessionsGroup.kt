package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString
import io.github.droidkaigi.confsched2018.util.ext.toLocalDate
import org.threeten.bp.Period
import java.util.*

class DateSessionsGroup(private val fragment: Fragment) : UpdatingGroup() {
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

    fun getDateCountSinceBeginOrNull(firstPosition: Int): Int? {
        val firstDay = getDateOrNull(0) ?: return null
        val date = getDateOrNull(firstPosition) ?: return null
        return Period.between(firstDay.toLocalDate(), date.toLocalDate()).days + 1
    }

    fun getDateOrNull(position: Int): Date? {
        if (position < 0) return null

        var item = getItemOrNull(position) ?: return null
        item = item as? SessionItem ?: getItemOrNull(position + 1) ?: return null
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
