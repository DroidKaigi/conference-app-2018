package io.github.droidkaigi.confsched2018.util.ext

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IntegerRes
import android.support.v4.content.ContextCompat
import android.view.WindowManager

fun Context.displaySize(): Size {
    val point = Point()
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    manager.defaultDisplay.getSize(point)
    return Size(point.x, point.y)
}

fun Context.color(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)

fun Context.bool(@BoolRes boolRes: Int): Boolean = resources.getBoolean(boolRes)

fun Context.integer(@IntegerRes integerRes: Int): Int = resources.getInteger(integerRes)

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable {
    return ContextCompat.getDrawable(this, drawableRes)!!
}

data class Size(val width: Int, val height: Int)
