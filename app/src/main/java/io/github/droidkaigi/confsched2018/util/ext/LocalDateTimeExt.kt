package io.github.droidkaigi.confsched2018.util.ext

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun LocalDateTime.toUnixMills(): Long = atZone(ZoneId.systemDefault())
        .toInstant().toEpochMilli()
