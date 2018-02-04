package io.github.droidkaigi.confsched2018.util.ext

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs

fun LinearLayoutManager.saveScrollPositionToPrefs() {
    val savedState = onSaveInstanceState()
    val parcel = Parcel.obtain()
    savedState.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    parcel.setDataPosition(0)

    val anchorPosition = parcel.readInt()
    val anchorOffset = parcel.readInt()

    parcel.recycle()

    Prefs.previousSessionScrollPosition = anchorPosition
    Prefs.previousSessionScrollOffset = anchorOffset
}

fun LinearLayoutManager.restoreScrollPositionFromPrefs() {
    val previousScrollPosition = Prefs.previousSessionScrollPosition
    val previousScrollOffset = Prefs.previousSessionScrollOffset

    if (previousScrollPosition < 0) return

    val parcel = Parcel.obtain()
    parcel.writeInt(previousScrollPosition)
    parcel.writeInt(previousScrollOffset)
    parcel.writeInt(0)
    parcel.setDataPosition(0)

    val savedState = LinearLayoutManager.SavedState.CREATOR.createFromParcel(parcel)

    parcel.recycle()

    onRestoreInstanceState(savedState)
}
