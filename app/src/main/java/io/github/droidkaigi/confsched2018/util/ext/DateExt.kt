package io.github.droidkaigi.confsched2018.util.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toReadableDateTimeString() = "${toReadableDateString()} ${toReadableTimeString()}"

fun Date.toReadableDateString() = SimpleDateFormat("MM/dd", Locale.getDefault()).format(this)

fun Date.toReadableTimeString() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
