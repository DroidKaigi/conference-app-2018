package io.github.droidkaigi.confsched2018.util.ext

import android.content.res.Resources
import android.support.annotation.DimenRes
import android.util.TypedValue

fun Resources.getFloat(@DimenRes resourceId: Int): Float {
    val outValue = TypedValue()
    getValue(resourceId, outValue, true)
    return outValue.float
}
