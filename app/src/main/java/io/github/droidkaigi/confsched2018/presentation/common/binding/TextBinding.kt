package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import io.github.droidkaigi.confsched2018.model.Date
import io.github.droidkaigi.confsched2018.model.toReadableDateTimeString
import io.github.droidkaigi.confsched2018.model.toReadableTimeString

@BindingAdapter(value = ["bind:startDate", "bind:endDate"])
fun TextView.setPeriodText(startDate: Date, endDate: Date) {
    text = "${startDate.toReadableTimeString()} - ${endDate.toReadableTimeString()}"
}

@BindingAdapter(value = ["android:text"])
fun TextView.setPeriodText(date: Date) {
    text = date.toReadableDateTimeString()
}
