package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val dateFormat = SimpleDateFormat("MM/dd", Locale.US)
private val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

fun Date.toReadableDateTimeString() = "${toReadableDateString()} ${toReadableTimeString()}"

fun Date.toReadableDateString() =
        synchronized(Prefs) {
            dateFormat.timeZone = timeZone()
            dateFormat.format(this)
        }

fun Date.toReadableTimeString() =
        synchronized(Prefs) {
            timeFormat.timeZone = timeZone()
            timeFormat.format(this)
        }

private fun timeZone(): TimeZone {
    return if (Prefs.enableLocalTime) {
        TimeZone.getDefault()
    } else {
        TimeZone.getTimeZone("Asia/Tokyo")
    }
}
