package io.github.droidkaigi.confsched2018.util.ext

import android.content.Context
import android.graphics.Point
import android.view.WindowManager


fun Context.getDisplaySize(): Size {
    val point = Point()
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    manager.defaultDisplay.getSize(point)
    return Size(point.x, point.y)
}

data class Size(val width: Int, val height: Int)
