package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class TopicsGroup(val dataBindingComponent: FragmentDataBindingComponent) : Section() {

    fun updateTopics(topics: List<Topic>) {
        val list = mutableListOf<Item<*>>()
        topics.sortedBy { it.id }.mapTo(list) { topic ->
            TopicItem(topic, dataBindingComponent)
        }
        update(list)
    }
}
