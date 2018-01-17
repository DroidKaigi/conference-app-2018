package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import javax.inject.Inject

class FragmentBindingAdapters @Inject
constructor(internal val fragment: Fragment) {
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
                .with(fragment)
                .load(url)
                .placeholder(placeHolder)
                .override(width.toInt(), height.toInt())
                .dontAnimate()
                .transform(CircleCrop())
                .into(this)
    }
}
