package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpecialSessionBinding
import io.github.droidkaigi.confsched2018.model.Session

data class SpecialSessionItem(
        override val session: Session.SpecialSession,
        private val isShowDayNumber: Boolean = false
) : BindableItem<ItemSpecialSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemSpecialSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.isShowDayNumber = isShowDayNumber
    }

    override fun getLayout(): Int = R.layout.item_special_session
}
