package io.github.droidkaigi.confsched2018.presentation.search.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSearchSpeakerBinding
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

data class SearchResultSpeakerItem(
        val speaker: Speaker,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemSearchSpeakerBinding>(speaker.id.hashCode().toLong()) {
    override fun createViewHolder(itemView: View): ViewHolder<ItemSearchSpeakerBinding> {
        val viewDataBinding =
                DataBindingUtil.bind<ItemSearchSpeakerBinding>(itemView, dataBindingComponent)
        return ViewHolder(viewDataBinding)
    }

    override fun bind(viewBinding: ItemSearchSpeakerBinding, position: Int) {
        viewBinding.searchContent!!.speaker = speaker
    }

    override fun getLayout(): Int = R.layout.item_search_speaker
}
