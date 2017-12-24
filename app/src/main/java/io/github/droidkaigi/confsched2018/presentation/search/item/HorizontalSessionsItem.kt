package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchHorizontalSessionsBinding
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Session

class HorizontalSessionsItem(
        val level: Level,
        var sessions: List<Session>,
        private val onFavoriteClickListener: (Session) -> Unit = {},
        private val fragment: Fragment, private val scrollPositionMap: HashMap<Int, LevelSessionsGroup.PositionAndOffset>
) : BindableItem<ItemSearchHorizontalSessionsBinding>(
        level.id.toLong()
) {
    private val updatingGroup = UpdatingGroup()

    override fun bind(viewBinding: ItemSearchHorizontalSessionsBinding, position: Int) {
        val items = mutableListOf<Item<*>>()
        viewBinding.searchSessionsRecycler.swapAdapter(GroupAdapter<com.xwray.groupie.ViewHolder>().apply {
            add(updatingGroup)
        }, false)
        viewBinding.searchSessionsRecycler.apply {
            // Restore scroll position from HashMap
            val scroll = scrollPositionMap.getOrElse(position, { LevelSessionsGroup.PositionAndOffset(0, 0) })
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(scroll.position, scroll.offset)
        }
        sessions.forEach {
            items.add(HorizontalSessionItem(it, onFavoriteClickListener, fragment))
        }
        updatingGroup.update(items)
    }

    override fun unbind(holder: ViewHolder<ItemSearchHorizontalSessionsBinding>) {
        // Save scroll position to HashMap
        val linearLayoutManager = holder.binding.searchSessionsRecycler.layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()
        val x = linearLayoutManager.findViewByPosition(position).x - (linearLayoutManager.findViewByPosition(position).layoutParams as RecyclerView.LayoutParams).marginStart
        scrollPositionMap.put(linearLayoutManager.getPosition(holder.root), LevelSessionsGroup.PositionAndOffset(position, x.toInt()))
        super.unbind(holder)
    }

    override fun getLayout(): Int = R.layout.item_search_horizontal_sessions

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HorizontalSessionsItem

        if (level != other.level) return false
        if (sessions != other.sessions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = level.hashCode()
        result = 31 * result + sessions.hashCode()
        return result
    }
}
