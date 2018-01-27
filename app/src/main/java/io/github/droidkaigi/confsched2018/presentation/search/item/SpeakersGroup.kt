package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.Item
import com.xwray.groupie.Section
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.util.ext.toGroupId

class SpeakersSection : Section() {

    // Ignore Prefix for Group. ex) @mike -> mike
    private val groupIgnorePrefixes = charArrayOf('@')

    fun updateSpeakers(speakers: List<Speaker>) {
        val list = mutableListOf<Item<*>>()
        speakers.sortedBy { it.toGroupId(*groupIgnorePrefixes) }
                .mapTo(list) { speaker -> SpeakerItem(speaker) }
        update(list)
    }

    fun getGroupId(position: Int) : Char? {
        return getItemOrNull(position)
                ?.let { it as? SpeakerItem }
                ?.speaker?.toGroupId(*groupIgnorePrefixes)
    }

    private fun getItemOrNull(i: Int): Item<*>? {
        if (itemCount <= i) {
            return null
        }
        return getItem(i)
    }
}
