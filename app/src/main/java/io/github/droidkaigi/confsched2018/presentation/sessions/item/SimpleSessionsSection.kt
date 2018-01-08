package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Session

class SimpleSessionsSection(val fragment: Fragment) : Section() {
    fun updateSessions(
            sessions: List<Session>,
            onFavoriteClickListener: (Session.SpeechSession) -> Unit
    ) {
        val sessionItems = sessions.map {
            when (it) {
                is Session.SpeechSession -> {
                    SpeechSessionItem(it, onFavoriteClickListener, fragment, true) as Item<*>
                }
                is Session.SpecialSession -> {
                    SpecialSessionItem(it) as Item<*>
                }
            }
        }
        update(sessionItems)
    }
}
