package io.github.droidkaigi.confsched2018.util

import android.view.View

/**
 * A [View.OnClickListener] that debounces multiple clicks posted in the same frame.
 * A click on one button disables all buttons for that frame.
 *
 * Taken from ButterKnife:
 * https://github.com/JakeWharton/butterknife/blob/e78507711fe8a7c637ee61c44a7b09f1be8ff9f6/butterknife/src/main/java/butterknife/internal/DebouncingOnClickListener.java
 */
abstract class DebouncingOnClickListener : View.OnClickListener {
    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.post(ENABLE_AGAIN)
            doClick(v)
        }
    }

    abstract fun doClick(v: View)

    companion object {
        private var enabled = true
        private val ENABLE_AGAIN = Runnable { enabled = true }
    }
}
