package io.github.droidkaigi.confsched2018.model

import java.io.Serializable
import java.util.Date

data class SessionSchedule(
        val startTime: Date,
        val dayNumber: Int
): Comparable<SessionSchedule>, Serializable {
    override fun compareTo(other: SessionSchedule): Int = startTime.compareTo(other.startTime)
}
