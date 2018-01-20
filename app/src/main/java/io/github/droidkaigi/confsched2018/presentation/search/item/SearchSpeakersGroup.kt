package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Speaker

class SearchSpeakersSection : Section() {

    fun updateSpeakers(speakers: List<Speaker>, searchQuery: String = "") {
        val list = mutableListOf<Item<*>>()
        speakers.sortedBy { it.name }.mapTo(list) { speaker ->
            SearchResultSpeakerItem(speaker, searchQuery)
        }
        update(list)
    }
}
