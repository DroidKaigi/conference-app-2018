package io.github.droidkaigi.confsched2018.util

import io.github.droidkaigi.confsched2018.model.Lang
import java.util.*

fun lang(): Lang = if (Locale.JAPAN == Locale.getDefault()) {
    Lang.JA
} else {
    Lang.EN
}
