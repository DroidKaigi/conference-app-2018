package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session

class SimpleSessionsSection : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit,
            searchQuery: String = "",
            userIdInDetail: String? = null
    ) {
        val sessionItems = sessions.map {
            when (it) {
                is Session.SpeechSession -> {
                    @Suppress("USELESS_CAST")
                    SpeechSessionItem(
                            session = it,
                            onFavoriteClickListener = onFavoriteClickListener,
                            isShowDayNumber = true,
                            searchQuery = searchQuery,
                            userIdInDetail = userIdInDetail
                    ) as Item<*>
                }
                is Session.SpecialSession -> {
                    @Suppress("USELESS_CAST")
                    SpecialSessionItem(
                            session = it
                    ) as Item<*>
                }
            }
        }
        update(sessionItems)
    }
}
