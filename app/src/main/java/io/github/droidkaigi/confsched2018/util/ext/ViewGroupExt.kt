package io.github.droidkaigi.confsched2018.util.ext

import android.view.View
import android.view.ViewGroup

fun ViewGroup.eachChildView(function: (view: View) -> Unit) {
    (0 until childCount).forEach { function(getChildAt(it)) }
}
