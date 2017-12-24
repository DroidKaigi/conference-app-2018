package io.github.droidkaigi.confsched2018.util

object ViewSizeCalculator {
    fun calculateViewSizeByScreenAndCount(screenSize: Int, showItemCount: Float, itemMargin: Int = 0): Int {
        return ((screenSize - itemMargin) / showItemCount).toInt()
    }
}
