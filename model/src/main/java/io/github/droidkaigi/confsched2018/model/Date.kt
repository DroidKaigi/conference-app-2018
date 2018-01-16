package io.github.droidkaigi.confsched2018.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Date(
        optTimeInMills: Long? = null
) : Comparable<Date> {

    private val calendar: Calendar = Calendar.getInstance().apply {
        if (optTimeInMills != null) {
            timeInMillis = optTimeInMills
        }
    }

    val date: java.util.Date
        get() = calendar.time

    fun getDate() = calendar[Calendar.DAY_OF_MONTH]

    fun getMonth() = calendar[Calendar.MONTH]

    fun getFullYear() = calendar[Calendar.YEAR]

    fun getHours() = calendar[Calendar.HOUR_OF_DAY]

    fun getMinutes() = calendar[Calendar.MINUTE]

    fun getTime(): Number = calendar.timeInMillis

    override fun hashCode(): Int {
        return date.time.toInt()
    }

    override fun equals(other: Any?): Boolean =
            other is Date && other.calendar.time == calendar.time

    override fun compareTo(other: Date): Int = date.compareTo(other.date)
}

fun Date.toReadableDateTimeString() = "${toReadableDateString()} ${toReadableTimeString()}"

fun readableDateFormat() = SimpleDateFormat("MM/dd", Locale.getDefault())
fun readableTimeFormat() = SimpleDateFormat("HH:mm", Locale.getDefault())
fun Date.toReadableDateString() = readableDateFormat().format(date)
fun Date.toReadableTimeString() = readableTimeFormat().format(date)
fun parseDate(timeInMills: Long): Date = Date(timeInMills)
