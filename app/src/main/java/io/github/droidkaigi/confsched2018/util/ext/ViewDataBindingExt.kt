package io.github.droidkaigi.confsched2018.util.ext

import android.content.Context
import android.databinding.ViewDataBinding

val ViewDataBinding.context: Context
    get() = root.context
