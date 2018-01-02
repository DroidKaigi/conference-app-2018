package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.toReadableDateTimeString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString

@BindingAdapter(value = ["bind:startDate", "bind:endDate", "bind:postFix"])
fun TextView.setPeriodText(startDate: Date?, endDate: Date?, postFix: String) {
    startDate ?: return
    endDate ?: return
    text = context.getString(
            R.string.time_period,
            startDate.toReadableTimeString(),
            endDate.toReadableTimeString(),
            postFix
    )
}

@BindingAdapter(value = ["bind:startDate", "bind:endDate"])
fun TextView.setPeriodText(startDate: Date?, endDate: Date?) {
    setPeriodText(startDate, endDate, "")
}

@BindingAdapter(value = ["android:text"])
fun TextView.setDateText(date: Date?) {
    date ?: return
    text = date.toReadableDateTimeString()
}
