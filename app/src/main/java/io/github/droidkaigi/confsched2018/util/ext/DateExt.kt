package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.model.Date
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

fun Date.toLocalDateTime(): LocalDateTime =
        LocalDateTime.of(getFullYear(), getMonth(), getDate(), getHours(), getMinutes())

fun Date.toLocalDate(): LocalDate =
        LocalDate.of(getFullYear(), getMonth(), getDate())
