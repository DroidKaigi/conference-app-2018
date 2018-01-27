package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.model.Speaker

fun Speaker.toGroupId(vararg ignorePrefixes: Char): Char {
    return this.name
            .firstOrNull { c -> ignorePrefixes.all { !it.equals(c, true) } }
            ?.toUpperCase() ?: '*'
}
