package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session

class SimpleSessionsSection(val fragment: Fragment) : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit,
            searchQuery: String = ""
    ) {
        val sessionItems = sessions.map {
            when (it) {
                is Session.SpeechSession -> {
                    @Suppress("USELESS_CAST")
                    SpeechSessionItem(
                            it,
                            onFavoriteClickListener,
                            fragment,
                            true,
                            searchQuery
                    ) as Item<*>
                }
                is Session.SpecialSession -> {
                    @Suppress("USELESS_CAST")
                    SpecialSessionItem(it) as Item<*>
                }
            }
        }
        update(sessionItems)
    }
}
