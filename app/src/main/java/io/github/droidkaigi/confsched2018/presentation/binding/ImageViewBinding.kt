package io.github.droidkaigi.confsched2018.presentation.binding

import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.widget.ImageView

@BindingMethods(
        BindingMethod(type = ImageView::class,
                attribute = "app:srcCompat",
                method = "setImageDrawable"))
class ImageViewBinding
