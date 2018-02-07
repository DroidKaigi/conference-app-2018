package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpeechSessionBinding
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.ext.context
import io.github.droidkaigi.confsched2018.util.ext.drawable
import io.github.droidkaigi.confsched2018.util.lang

data class SpeechSessionItem(
        override val session: Session.SpeechSession,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit,
        private val onFeedbackListener: (Session.SpeechSession) -> Unit,
        private val isShowDayNumber: Boolean = false,
        private val searchQuery: String = "",
        private val simplify: Boolean = false,
        private val userIdInDetail: String? = null
) : BindableItem<ItemSpeechSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemSpeechSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.searchQuery = searchQuery
        viewBinding.topic.text = session.topic.name
        viewBinding.favorite.setOnClickListener {
            onFavoriteClickListener(session)
        }
        viewBinding.speakerSummary.setSpeakerIdInDetail(userIdInDetail)
        viewBinding.isShowDayNumber = isShowDayNumber
        viewBinding.simplify = simplify
        viewBinding.goToFeedback.setOnClickListener {
            onFeedbackListener(session)
        }
        session.message?.let { message ->
            viewBinding.message.text = if (lang() == Lang.JA) {
                message.jaMessage
            } else {
                message.enMessage
            }
        }

        val levelDrawable = viewBinding.context.drawable(when (session.level) {
            is Level.Beginner -> R.drawable.ic_beginner_lightgreen_20dp
            is Level.IntermediateOrExpert -> R.drawable.ic_intermediate_senior_bluegray_20dp
            is Level.Niche -> R.drawable.ic_niche_cyan_20dp
        })
        viewBinding.level.setImageDrawable(levelDrawable)
    }

    override fun getLayout(): Int = R.layout.item_speech_session
}
