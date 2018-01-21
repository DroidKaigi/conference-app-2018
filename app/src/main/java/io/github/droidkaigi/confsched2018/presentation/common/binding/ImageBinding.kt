package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.github.droidkaigi.confsched2018.util.CustomGlideApp

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

@BindingAdapter(
        value = [
            "loadImage",
            "placeHolder",
            "android:layout_width",
            "android:layout_height"
        ]
)
fun ImageView.loadImage(url: String?, placeHolder: Drawable, width: Float, height: Float) {
    url ?: return
    CustomGlideApp
            .with(this)
            .load(url)
            .placeholder(placeHolder)
            .override(width.toInt(), height.toInt())
            .dontAnimate()
            .transform(CircleCrop())
            .into(this)
}
