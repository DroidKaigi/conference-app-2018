package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.Session
import java.util.SortedMap

class FavoriteSessionsSection : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit
    ) {
        val sessionItems = sessions.map {
            FavoriteSessionItem(it as Session.SpeechSession, onFavoriteClickListener) as SessionItem
        }

        val dateSpeechSessionItemsMap: SortedMap<Date, List<SessionItem>> =
                sessionItems.groupBy { it.session.startTime }.toSortedMap()

        val dateSessions = arrayListOf<Item<*>>()
        dateSpeechSessionItemsMap.keys.forEach { startTime ->
            startTime ?: return@forEach
            val list = dateSpeechSessionItemsMap[startTime]

            val endTime = list!![0].session.endTime
            dateSessions.add(DateHeaderItem(startTime, endTime))
            @Suppress("UNCHECKED_CAST")
            dateSessions.addAll(list.toMutableList() as List<Item<*>>)
        }
        update(dateSessions)
    }

    fun getDateNumberOrNull(position: Int): Int? {
        if (position < 0) return null

        var item = getItemOrNull(position) ?: return null
        item = item as? SpeechSessionItem ?: getItemOrNull(position + 1) ?: return null
        return when (item) {
            is SessionItem -> {
                item.session.dayNumber
            }
            else -> null
        }
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }
}
