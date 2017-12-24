package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchSessionHeaderBinding
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.Level

data class SessionHeaderItem(
        val level: Level
) : BindableItem<ItemSearchSessionHeaderBinding>(
        level.id.toLong()
) {
    override fun bind(viewBinding: ItemSearchSessionHeaderBinding, position: Int) {
        viewBinding.title.text = level.getNameByLang(Lang.JA)
        viewBinding.subTitle.text = level.getNameByLang(Lang.EN)
    }

    override fun getLayout(): Int = R.layout.item_search_session_header
}
