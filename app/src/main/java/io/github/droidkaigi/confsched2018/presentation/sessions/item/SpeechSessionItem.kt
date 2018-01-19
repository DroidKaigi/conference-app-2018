package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpeechSessionBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.util.lang
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

data class SpeechSessionItem(
        override val session: Session.SpeechSession,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit,
        private val isShowDayNumber: Boolean = false,
        private val searchQuery: String = "",
        private val simplify: Boolean = false
) : BindableItem<ItemSpeechSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemSpeechSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.searchQuery = searchQuery
        viewBinding.topic.text = session.topic.name
        viewBinding.level.text = session.level.getNameByLang(lang())
        viewBinding.favorite.setOnClickListener {
            onFavoriteClickListener(session)
        }
        viewBinding.isShowDayNumber = isShowDayNumber
        viewBinding.simplify = simplify
        viewBinding.isFinished = isFinishedSession(session)
        viewBinding.goToQuestionnaire.setOnClickListener {
            //TODO: will implement this. Please check comments of issue #141
        }
    }

    override fun getLayout(): Int = R.layout.item_speech_session

    private fun isFinishedSession(session: Session.SpeechSession): Boolean {
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"))
        cal.timeInMillis = System.currentTimeMillis()
        val now: Date = cal.time
        return now.time.toInt() > session.endTime.getTime().toInt()
    }
}
