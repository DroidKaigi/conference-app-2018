package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import android.view.View
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemHorizontalSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ViewSizeCalculator.calculateViewSizeByScreenAndCount
import io.github.droidkaigi.confsched2018.util.ext.context
import io.github.droidkaigi.confsched2018.util.ext.displaySize
import io.github.droidkaigi.confsched2018.util.ext.getFloat

class HorizontalSessionItem(
        val session: Session.SpeechSession,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit,
        private val onItemClickListener: OnItemClickListener,
        private val fragment: Fragment
) : BindableItem<ItemHorizontalSessionBinding>(
        session.id.hashCode().toLong()
) {

    override fun createViewHolder(itemView: View): ViewHolder<ItemHorizontalSessionBinding> {
        val viewHolder = super.createViewHolder(itemView)
        val binding = viewHolder.binding

        val width = calculateViewSizeByScreenAndCount(
                binding.context.displaySize().width,
                binding.context.resources.getFloat(R.dimen.horizontal_visible_item_count))
        itemView.layoutParams.width = width
        return viewHolder
    }

    override fun bind(viewBinding: ItemHorizontalSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.root.setOnClickListener { view ->
            onItemClickListener.onItemClick(this, view)
        }
        viewBinding.favorite.setOnClickListener {
            onFavoriteClickListener(session)
        }
    }

    override fun getLayout(): Int = R.layout.item_horizontal_session

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HorizontalSessionItem

        if (session != other.session) return false

        return true
    }

    override fun hashCode(): Int {
        return session.hashCode()
    }
}
