package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.BuildConfig
import io.github.droidkaigi.confsched2018.model.Session

class TimeBasedSessionsSection : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit
    ) {
        val sessionItems = sessions.sortedWith(SessionComparator).map {
            when (it) {
                is Session.SpeechSession -> {
                    SpeechSessionItem(
                            session = it,
                            onFavoriteClickListener = onFavoriteClickListener
                    )
                }
                is Session.SpecialSession -> {
                    @Suppress("USELESS_CAST")
                    SpecialSessionItem(
                            session = it
                    ) as Item<*>
                }
            }
        }

        if (BuildConfig.DEBUG) {
            if (sessionItems.distinctBy { (it as SessionItem).session.startTime }.size != 1) {
                throw IllegalStateException("Different start time was found")
            }
        }

        update(sessionItems)
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }

    private object SessionComparator : Comparator<Session> {
        override fun compare(o1: Session?, o2: Session?): Int {
            return when {
                o1 == o2 -> 0
                o1 == null -> Int.MIN_VALUE
                o2 == null -> Int.MAX_VALUE
                else -> o2.toOrder() - o1.toOrder()
            }
        }

        private fun Session.toOrder(): Int = when (this) {
            is Session.SpeechSession -> {
                room.id
            }
            is Session.SpecialSession -> {
                room?.id ?: Int.MAX_VALUE
            }
        }
    }
}
