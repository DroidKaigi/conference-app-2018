package io.github.droidkaigi.confsched2018.util

import io.github.droidkaigi.confsched2018.model.Lang
import java.util.Locale

fun lang(): Lang = if (Locale.JAPAN == Locale.getDefault()) {
    Lang.JA
} else {
    Lang.EN
}
