package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchSessionHeaderBinding

data class SessionHeaderItem(
        val level: String
) : BindableItem<ItemSearchSessionHeaderBinding>(
        level.hashCode().toLong()
) {
    override fun bind(viewBinding: ItemSearchSessionHeaderBinding, position: Int) {
        viewBinding.title.text = level
    }

    override fun getLayout(): Int = R.layout.item_search_session_header
}
