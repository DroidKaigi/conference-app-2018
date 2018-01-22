package io.github.droidkaigi.confsched2018.util.ext

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun LocalDateTime.toUnixMills(): Long {
    val timeZone = toZoneId("JST")
    return atZone(timeZone).toInstant().toEpochMilli()
}

private fun toZoneId(id: String, shortIds: MutableMap<String, String> = ZoneId.SHORT_IDS) =
        ZoneId.of(id, shortIds)
