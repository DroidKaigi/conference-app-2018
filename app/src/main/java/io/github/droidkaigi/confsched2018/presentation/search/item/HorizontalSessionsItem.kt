package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchHorizontalSessionsBinding
import io.github.droidkaigi.confsched2018.model.Session

data class HorizontalSessionsItem(
        val sessions: List<Session>,
        private val onFavoriteClickListener: (Session) -> Unit = {},
        private val fragment: Fragment
) : BindableItem<ItemSearchHorizontalSessionsBinding>(
        sessions.hashCode().toLong()
) {
    override fun bind(viewBinding: ItemSearchHorizontalSessionsBinding, position: Int) {
    }

    override fun getLayout(): Int = R.layout.item_search_horizontal_sessions

}
