package io.github.droidkaigi.confsched2018.presentation.speaker.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SessionItem

class SimpleSessionsSection(val fragment: Fragment) : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session) -> Unit
    ) {
        val sessionItems = sessions.map {
            SessionItem(it, onFavoriteClickListener, fragment, true)
        }
        update(sessionItems)
    }
}
