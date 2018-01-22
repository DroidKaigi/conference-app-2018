package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Topic

class TopicsGroup : Section() {

    fun updateTopics(topics: List<Topic>) {
        val list = mutableListOf<Item<*>>()
        topics.sortedBy { it.id }.mapTo(list) { topic ->
            TopicItem(topic)
        }
        update(list)
    }
}
