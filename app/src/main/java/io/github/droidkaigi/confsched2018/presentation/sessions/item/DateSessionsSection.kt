package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.toReadableDateString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString
import java.util.SortedMap

class DateSessionsSection(private val fragment: Fragment) : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit
    ) {
        val sessionItems = sessions.map {
            when (it) {
                is Session.SpeechSession -> {
                    SpeechSessionItem(it, onFavoriteClickListener, fragment) as SessionItem
                }
                is Session.SpecialSession -> {
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

            dateSessions.add(DateHeaderItem(key))
            dateSessions.addAll(list?.toMutableList().orEmpty() as List<Item<*>>)
        }
        update(dateSessions)
    }

    fun getDateNumberOrNull(position: Int): Int? {
        if (position < 0) return null

        var item = getItemOrNull(position) ?: return null
        item = item as? SpeechSessionItem ?: getItemOrNull(position + 1) ?: return null
        item as? SpeechSessionItem ?: return null

        return item.session.dayNumber
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }
}
