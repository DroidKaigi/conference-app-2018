package io.github.droidkaigi.confsched2018.presentation.search.item

import com.xwray.groupie.Item
import com.xwray.groupie.UpdatingGroup
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class SpeakersGroup(val dataBindingComponent: FragmentDataBindingComponent) : UpdatingGroup() {

    fun updateSpeakers(
            speakers: List<Speaker>
    ) {
        val list = mutableListOf<Item<*>>()
        speakers.sortedBy { it.name }.mapTo(list) { speaker ->
            SpeakerItem(speaker, dataBindingComponent)
        }
        update(list)
    }
}
