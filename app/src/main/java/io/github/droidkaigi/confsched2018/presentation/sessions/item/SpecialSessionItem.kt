package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpecialSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ext.color
import io.github.droidkaigi.confsched2018.util.ext.context
import java.util.Date

data class SpecialSessionItem(
        override val session: Session.SpecialSession,
        private val isShowDayNumber: Boolean = false
) : BindableItem<ItemSpecialSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemSpecialSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.isShowDayNumber = isShowDayNumber

        val now = Date()
        val backgroundColor = when {
            now.before(session.startTime) -> viewBinding.context.color(R.color
                    .card_background_color)
            now.after(session.endTime) -> viewBinding.context.color(R.color.card_background_color)
            else -> viewBinding.context.color(R.color.highlight_text)
        }
        viewBinding.root.setBackgroundColor(backgroundColor)
    }

    override fun getLayout(): Int = R.layout.item_special_session
}
