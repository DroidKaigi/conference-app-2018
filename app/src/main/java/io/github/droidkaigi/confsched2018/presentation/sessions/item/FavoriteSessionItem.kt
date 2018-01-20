package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemFavoriteSessionBinding
import io.github.droidkaigi.confsched2018.model.Session

data class FavoriteSessionItem(
        override val session: Session.SpeechSession,
        private val onFavoriteClickListener: (Session.SpeechSession) -> Unit
) : BindableItem<ItemFavoriteSessionBinding>(
        session.id.toLong()
), SessionItem {

    override fun bind(viewBinding: ItemFavoriteSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.favorite.setOnClickListener {
            onFavoriteClickListener(session)
        }
    }

    override fun getLayout(): Int = R.layout.item_favorite_session
}
