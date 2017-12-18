package io.github.droidkaigi.confsched2018.util.ext

import android.view.View

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.isGone() = visibility == View.GONE
fun View.isVisible() = visibility == View.VISIBLE



