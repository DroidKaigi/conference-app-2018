package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Session

class LevelSessionsGroup(private val fragment: Fragment) : UpdatingGroup() {
    data class PositionAndOffset(val position: Int, val offset: Int)

    val levelScrollPositionMap = hashMapOf<Int, PositionAndOffset>()

    fun updateSessions(
            levelSessions: Map<Level, List<Session>>,
            onFavoriteClickListener: (Session) -> Unit = {}
    ) {
        val list = mutableListOf<Item<*>>()
        levelSessions.keys.sortedBy { it.id }.map { level ->
            list.add(SessionHeaderItem(level))
            list.add(HorizontalSessionsItem(level, levelSessions[level]!!, onFavoriteClickListener, fragment, levelScrollPositionMap))
        }
        update(list)
    }

}
