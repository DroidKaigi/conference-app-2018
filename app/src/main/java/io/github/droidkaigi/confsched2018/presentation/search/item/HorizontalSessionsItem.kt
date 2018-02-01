package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.OnItemLongClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchHorizontalSessionsBinding
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Session

class HorizontalSessionsItem(
        val level: Level,
        var sessions: List<Session.SpeechSession>,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit,
        private val fragment: Fragment,
        private val scrollPositionMap: HashMap<Int, LevelSessionsSection.PositionAndOffset>
) : BindableItem<ItemSearchHorizontalSessionsBinding>(
        level.id.toLong()
) {
    private val section = Section()
    private lateinit var onItemClickListener: OnItemClickListener

    override fun bind(
            holder: ViewHolder<ItemSearchHorizontalSessionsBinding>,
            position: Int,
            payloads: MutableList<Any>,
            onItemClickListener: OnItemClickListener?,
            onItemLongClickListener: OnItemLongClickListener?
    ) {
        this.onItemClickListener = onItemClickListener!!
        super.bind(holder, position, payloads, onItemClickListener, onItemLongClickListener)
    }

    override fun bind(viewBinding: ItemSearchHorizontalSessionsBinding, position: Int) {
        val items = mutableListOf<Item<*>>()
        val groupAdapter = GroupAdapter<com.xwray.groupie.ViewHolder>().apply {
            add(section)
        }
        viewBinding.searchSessionsRecycler.swapAdapter(groupAdapter, false)
        viewBinding.searchSessionsRecycler.apply {
            // Restore scroll position from HashMap
            val scroll = scrollPositionMap.getOrElse(position, {
                LevelSessionsSection.PositionAndOffset(0, 0)
            })
            val linearLayoutManager = layoutManager as LinearLayoutManager
            linearLayoutManager.scrollToPositionWithOffset(scroll.position, scroll.offset)
            isNestedScrollingEnabled = false
        }
        sessions.forEach {
            items.add(HorizontalSessionItem(
                    it,
                    onFavoriteClickListener,
                    onItemClickListener,
                    fragment
            ))
        }
        section.update(items)
    }

    override fun unbind(holder: ViewHolder<ItemSearchHorizontalSessionsBinding>) {
        // Save scroll position to HashMap
        val searchSessionsRecycler = holder.binding.searchSessionsRecycler
        val linearLayoutManager = searchSessionsRecycler.layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()
        val viewByPosition = linearLayoutManager.findViewByPosition(position)
        val layoutParams = viewByPosition.layoutParams as RecyclerView.LayoutParams
        val x = viewByPosition.x - layoutParams.marginStart
        scrollPositionMap.put(
                linearLayoutManager.getPosition(holder.root),
                LevelSessionsSection.PositionAndOffset(position, x.toInt())
        )
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
