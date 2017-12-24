package io.github.droidkaigi.confsched2018.presentation.search.item

import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemHorizontalSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ViewSizeCalculator.calculateViewSizeByScreenAndCount
import io.github.droidkaigi.confsched2018.util.ext.context
import io.github.droidkaigi.confsched2018.util.ext.getDisplaySize
import io.github.droidkaigi.confsched2018.util.ext.getFloat
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.lang

class HorizontalSessionItem(
        val session: Session,
        private val onFavoriteClickListener: (Session) -> Unit = {},
        private val fragment: Fragment
) : BindableItem<ItemHorizontalSessionBinding>(
        session.id.hashCode().toLong()
) {

    override fun createViewHolder(itemView: View): ViewHolder<ItemHorizontalSessionBinding> {
        val viewHolder = super.createViewHolder(itemView)
        val binding = viewHolder.binding

        val width = calculateViewSizeByScreenAndCount(
                binding.context.getDisplaySize().width,
                binding.context.resources.getFloat(R.dimen.horizontal_visible_item_count))
        itemView.layoutParams.width = width
        itemView.layoutParams.height = binding.context.resources.getDimensionPixelSize(R.dimen.search_session_item_height)

        return viewHolder
    }

    override fun bind(viewBinding: ItemHorizontalSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.level.text = session.level.getNameByLang(lang())
        val speakerImages = arrayOf(
                viewBinding.speakerImage1,
                viewBinding.speakerImage2,
                viewBinding.speakerImage3,
                viewBinding.speakerImage4,
                viewBinding.speakerImage5
        )
        speakerImages.forEachIndexed { index, imageView ->
            if (index < session.speakers.size) {
                imageView.visibility = View.VISIBLE
                val size = viewBinding.root.resources.getDimensionPixelSize(R.dimen.speaker_image)
                CustomGlideApp
                        .with(fragment)
                        .load(session.speakers[index].imageUrl)
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .override(size, size)
                        .dontAnimate()
                        .transform(CircleCrop())
                        .into(imageView)
            } else {
                imageView.toGone()
            }
        }

        viewBinding.speakers.text = session.speakers.joinToString { it.name }
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
