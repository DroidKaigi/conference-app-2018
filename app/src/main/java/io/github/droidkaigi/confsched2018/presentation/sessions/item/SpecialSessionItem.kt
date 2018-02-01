package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.view.View
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
        when {
            now.before(session.startTime) -> {
                viewBinding.root.setBackgroundColor(viewBinding.context.color(R.color
                        .card_background_color))
                viewBinding.currentBadge.visibility = View.GONE
            }
            now.after(session.endTime) -> {
                viewBinding.root.setBackgroundColor(viewBinding.context.color(R.color
                        .card_background_color))
                viewBinding.currentBadge.visibility = View.GONE
            }
            else -> {
                viewBinding.root.setBackgroundColor(viewBinding.context.color(R.color
                        .current_card_background_color))
                viewBinding.currentBadge.visibility = View.VISIBLE
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_special_session
}
