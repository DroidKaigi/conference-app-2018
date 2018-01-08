package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.support.v4.graphics.drawable.DrawableCompat

@BindingAdapter("tintCompat")
fun FloatingActionButton.setTintCompat(colorStateList: ColorStateList) {
    var icon: Drawable? = drawable
    if (icon != null) {
        icon = DrawableCompat.wrap(icon)
        DrawableCompat.setTintList(icon!!, colorStateList)
        setImageDrawable(icon)
    }
}
