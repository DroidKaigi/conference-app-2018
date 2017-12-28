package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.toReadableDateTimeString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString

@BindingAdapter(value = ["bind:startDate", "bind:endDate"])
fun TextView.setPeriodText(startDate: Date?, endDate: Date?) {
    startDate ?: return
    endDate ?: return
    text = context.getString(R.string.time_period, startDate.toReadableTimeString(), endDate.toReadableTimeString())
}

@BindingAdapter(value = ["android:text"])
fun TextView.setPeriodText(date: Date?) {
    date ?: return
    text = date.toReadableDateTimeString()
}
