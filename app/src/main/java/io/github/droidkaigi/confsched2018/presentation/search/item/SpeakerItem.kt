package io.github.droidkaigi.confsched2018.presentation.search.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSpeakerBinding
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class SpeakerItem(
        val speaker: Speaker,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemSpeakerBinding>(speaker.id.hashCode().toLong()) {
    override fun createViewHolder(itemView: View): ViewHolder<ItemSpeakerBinding> {
        val viewDataBinding =
                DataBindingUtil.bind<ItemSpeakerBinding>(itemView, dataBindingComponent)
        return ViewHolder(viewDataBinding)
    }

    override fun bind(viewBinding: ItemSpeakerBinding, position: Int) {
        viewBinding.speaker = speaker
    }

    override fun getLayout(): Int = R.layout.item_speaker

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpeakerItem

        if (speaker != other.speaker) return false

        return true
    }

    override fun hashCode(): Int {
        return speaker.hashCode()
    }
}
