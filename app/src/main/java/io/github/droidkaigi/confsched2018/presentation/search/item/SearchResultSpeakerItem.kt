package io.github.droidkaigi.confsched2018.presentation.search.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchSpeakerBinding
import io.github.droidkaigi.confsched2018.model.Speaker

data class SearchResultSpeakerItem(
        val speaker: Speaker,
        private val searchQuery: String = ""
) : BindableItem<ItemSearchSpeakerBinding>(speaker.id.hashCode().toLong()) {
    override fun createViewHolder(itemView: View): ViewHolder<ItemSearchSpeakerBinding> {
        val viewDataBinding =
                DataBindingUtil.bind<ItemSearchSpeakerBinding>(itemView)
        return ViewHolder(viewDataBinding)
    }

    override fun bind(viewBinding: ItemSearchSpeakerBinding, position: Int) {
        viewBinding.searchContent?.speaker = speaker
        viewBinding.searchQuery = searchQuery
    }

    override fun getLayout(): Int = R.layout.item_search_speaker
}
