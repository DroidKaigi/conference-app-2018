package io.github.droidkaigi.confsched2018.util.ext

import android.widget.TextView

// Prevent requestLayout()
fun TextView.setTextIfChanged(newText: String) {
    if (newText != text) {
        text = newText
    }
}

