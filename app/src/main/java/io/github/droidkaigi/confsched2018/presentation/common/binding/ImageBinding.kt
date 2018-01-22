package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView

@BindingMethods(
        BindingMethod(type = ImageView::class,
                attribute = "srcCompat",
                method = "setImageDrawable"))
class ImageBinding

@BindingAdapter("colorTint", "srcCompat")
fun ImageView.setColorTint(@ColorInt color: Int, drawable: Drawable) {
    val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
    DrawableCompat.setTint(wrappedDrawable, color)
    setImageDrawable(wrappedDrawable)
}
