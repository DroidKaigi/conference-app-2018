package io.github.droidkaigi.confsched2018.util.ext

import io.github.droidkaigi.confsched2018.model.Speaker

/**
 * Make sort key for search speakers list.
 *
 * sort key is speaker name that is upper cased and applied ignore-prefixes.
 * ex) @mike@github.net -> MIKE@GITHUB.NET
 */
fun Speaker.toSortKey(vararg ignorePrefixes: Char): String {
    return this.name
            .dropWhile { c -> ignorePrefixes.any { it.equals(c, true) } }
            .toUpperCase()
}
