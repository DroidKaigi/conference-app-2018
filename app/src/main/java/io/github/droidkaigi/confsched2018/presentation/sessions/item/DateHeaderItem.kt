package io.github.droidkaigi.confsched2018.presentation.sessions.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemDateHeaderBinding

data class DateHeaderItem(
        private val startDateTimePair: ReadableDateTimePair,
        private val endDateTimePair: ReadableDateTimePair
) : BindableItem<ItemDateHeaderBinding>(
        startDateTimePair.hashCode().toLong()
) {
    override fun bind(viewBinding: ItemDateHeaderBinding, position: Int) {
        viewBinding.periodText.text = viewBinding.root.context.getString(
                R.string.time_period,
                startDateTimePair.time,
                endDateTimePair.time
        )
    }

    override fun getLayout(): Int = R.layout.item_date_header
}

data class ReadableDateTimePair(
        val date: String,
        val time: String
) : Comparable<ReadableDateTimePair> {
    override fun compareTo(other: ReadableDateTimePair): Int {
        if (date > other.date) return 1
        if (date < other.date) return -1
        if (time > other.time) return 1
        if (time < other.time) return -1
        return 0
    }
}
