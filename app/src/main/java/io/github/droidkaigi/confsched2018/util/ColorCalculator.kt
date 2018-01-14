package io.github.droidkaigi.confsched2018.util

import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils

object ColorCalculator {

    @JvmStatic
    fun calculateColor(fraction: Float, @ColorInt color: Int): Int {
        return ColorUtils.setAlphaComponent(color, (255 * fraction).toInt())
    }
}
