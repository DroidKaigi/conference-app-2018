package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.toReadableDateTimeString() = "${toReadableDateString()} ${toReadableTimeString()}"

fun Date.toReadableDateString() = SimpleDateFormat("MM/dd", Locale.US).apply {
    timeZone = timeZone()
}.format(this)

fun Date.toReadableTimeString() = SimpleDateFormat("HH:mm", Locale.US).apply {
    timeZone = timeZone()
}.format(this)

private fun timeZone(): TimeZone {
    return if (Prefs.enableLocalTime) {
        TimeZone.getDefault()
    } else {
        TimeZone.getTimeZone("Asia/Tokyo")
    }
}
