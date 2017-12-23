package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.lang

data class SessionItem(
        val session: Session,
        private val onFavoriteClickListener: (Session) -> Unit = {},
        private val fragment: Fragment
) : BindableItem<ItemSessionBinding>(
        session.id.toLong()
) {

    override fun bind(viewBinding: ItemSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.topic.text = session.getTopic(lang())
        viewBinding.level.text = session.getLevel(lang())
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

    override fun getLayout(): Int = R.layout.item_session
}
