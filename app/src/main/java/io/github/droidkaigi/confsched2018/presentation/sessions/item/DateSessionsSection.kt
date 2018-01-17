package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString
import java.util.SortedMap

class DateSessionsSection : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit,
            simplify: Boolean = false
    ) {
        val sessionItems = sessions.map {
            when (it) {
                is Session.SpeechSession -> {
                    @Suppress("USELESS_CAST")
                    SpeechSessionItem(
                            it, onFavoriteClickListener, simplify) as SessionItem
                }
                is Session.SpecialSession -> {
                    @Suppress("USELESS_CAST")
                    SpecialSessionItem(it) as SessionItem
                }
            }
        }

        val dateSpeechSessionItemsMap: SortedMap<ReadableDateTimePair, List<SessionItem>> =
                sessionItems.groupBy {
                    ReadableDateTimePair(it.session.startTime.toReadableDateString(),
                            it.session.startTime.toReadableTimeString())
                }.toSortedMap()

        val dateSessions = arrayListOf<Item<*>>()
        dateSpeechSessionItemsMap.keys.forEach { key ->
            key ?: return@forEach
            val list = dateSpeechSessionItemsMap[key]

            val endTime = list!![0].session.endTime
            val endDateTimePair = ReadableDateTimePair(endTime.toReadableDateString(),
                    endTime.toReadableTimeString())
            dateSessions.add(DateHeaderItem(key, endDateTimePair))
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
            is SpeechSessionItem -> {
                item.session.dayNumber
            }
            is SpecialSessionItem -> {
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
