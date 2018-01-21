package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.TimeZone

fun LocalDateTime.toUnixMills(): Long {
    val defaultTimeZone = TimeZone.getDefault()
    val timeZone = if (Prefs.enableLocalTime) {
        toZoneId(defaultTimeZone.id)
    } else {
        toZoneId("JST")
    }
    return atZone(timeZone).toInstant().toEpochMilli()
}

private fun toZoneId(id: String, shortIds: Map<String, String> = ZoneId.SHORT_IDS) = ZoneId.of(id,
        shortIds)
