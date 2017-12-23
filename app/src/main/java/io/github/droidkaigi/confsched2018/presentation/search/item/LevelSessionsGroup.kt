package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import io.github.droidkaigi.confsched2018.model.Session

class LevelSessionsGroup(private val fragment: Fragment) : UpdatingGroup() {
    fun updateSessions(
            levelSessions: Map<String, List<Session>>,
            onFavoriteClickListener: (Session) -> Unit = {}
    ) {
        val items = mutableListOf<Item<*>>()
        levelSessions.keys.forEach { level ->
            items.add(SessionHeaderItem(level))
            items.add(HorizontalSessionsItem(levelSessions[level]!!, onFavoriteClickListener, fragment))
        }
        update(items)
    }
}
