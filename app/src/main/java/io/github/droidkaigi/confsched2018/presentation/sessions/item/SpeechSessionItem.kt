package io.github.droidkaigi.confsched2018.presentation.sessions.item

import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpeechSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.lang

data class SpeechSessionItem(
        override val session: Session.SpeechSession,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit,
        private val fragment: Fragment,
        private val isShowDayNumber: Boolean = false
) : BindableItem<ItemSpeechSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemSpeechSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.topic.text = session.topic.name
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
        viewBinding.isShowDayNumber = isShowDayNumber
    }

    override fun getLayout(): Int = R.layout.item_speech_session
}
