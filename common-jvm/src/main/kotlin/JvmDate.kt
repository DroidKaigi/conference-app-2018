package io.github.droidkaigi.confsched2018.model

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

actual class Date {
    private val calendar: Calendar

    actual constructor() {
        calendar = Calendar.getInstance()
    }

    constructor(timeInMills: Long) {
        calendar = Calendar.getInstance().apply {
            timeInMillis = timeInMills
        }
    }

    val date: java.util.Date
        get() = calendar.time

    actual fun getDate() = calendar[DAY_OF_MONTH]

    actual fun getMonth() = calendar[MONTH]

    actual fun getFullYear() = calendar[YEAR]

    actual fun getHours() = calendar[HOUR_OF_DAY]

    actual fun getMinutes() = calendar[MINUTE]

    actual fun getTime(): Number = calendar.timeInMillis

    override fun hashCode(): Int {
        return date.time.toInt()
    }

    override fun equals(other: Any?): Boolean =
            other is Date && other.calendar.time == calendar.time
}

actual operator fun Date.compareTo(otherDate: Date): Int = date.compareTo(otherDate.date)

fun readableDateFormat() = SimpleDateFormat("MM/dd", Locale.getDefault())
fun readableTimeFormat() = SimpleDateFormat("HH:mm", Locale.getDefault())
actual fun Date.toReadableDateString() = readableDateFormat().format(date)
actual fun Date.toReadableTimeString() = readableTimeFormat().format(date)
actual fun parseDate(timeInMills: Long): Date = Date(timeInMills)
