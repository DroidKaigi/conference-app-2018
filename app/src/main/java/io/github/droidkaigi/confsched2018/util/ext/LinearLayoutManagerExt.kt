package io.github.droidkaigi.confsched2018.util.ext

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager

fun LinearLayoutManager.getScrollState(): ScrollState {
    val savedState = onSaveInstanceState()
    val parcel = Parcel.obtain()
    savedState.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    parcel.setDataPosition(0)

    val scrollState = ScrollState(
            anchorPosition = parcel.readInt(),
            anchorOffset = parcel.readInt())
    parcel.recycle()

    return scrollState
}

fun LinearLayoutManager.restoreScrollState(scrollState: ScrollState) {
    if (scrollState.anchorPosition < 0) return

    val parcel = Parcel.obtain()
    parcel.writeInt(scrollState.anchorPosition)
    parcel.writeInt(scrollState.anchorOffset)
    parcel.writeInt(0)
    parcel.setDataPosition(0)

    val savedState = LinearLayoutManager.SavedState.CREATOR.createFromParcel(parcel)

    parcel.recycle()

    onRestoreInstanceState(savedState)
}

class ScrollState(val anchorPosition: Int, val anchorOffset: Int)
