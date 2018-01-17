package io.github.droidkaigi.confsched2018.presentation.search.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemTopicBinding
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.util.lang

data class TopicItem(
        val topic: Topic,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemTopicBinding>(topic.id.toLong()) {

    override fun createViewHolder(itemView: View): ViewHolder<ItemTopicBinding> {
        val viewDataBinding = DataBindingUtil.bind<ItemTopicBinding>(itemView, dataBindingComponent)
        return ViewHolder(viewDataBinding)
    }

    override fun bind(viewBinding: ItemTopicBinding, position: Int) {
        if (lang() == Lang.JA) {
            viewBinding.topicName.text = topic.getNameByLang(Lang.JA)
            viewBinding.topicTranslation.text = topic.getNameByLang(Lang.EN)
        } else {
            viewBinding.topicName.text = topic.getNameByLang(Lang.EN)
            viewBinding.topicTranslation.text = topic.getNameByLang(Lang.JA)
        }
    }

    override fun getLayout(): Int = R.layout.item_topic
}
