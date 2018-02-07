package io.github.droidkaigi.confsched2018.util.ext

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.view.forEach
import timber.log.Timber

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        menuView.forEach {
            val item = it as BottomNavigationItemView
            // NOTE: This removing the BottomNavigationItem labels including small and large text container.
            item.removeViewAt(1)

            // set the gravity of item icon to Center.
            val lp = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            lp.gravity = Gravity.CENTER
            item.getChildAt(0).layoutParams = lp

            // remove the default top margin.
            val topMargin = item.javaClass.getDeclaredField("mDefaultMargin")
            topMargin.isAccessible = true
            topMargin.setInt(item, 0)
            topMargin.isAccessible = false

            item.setShiftingMode(false)
            // set once again checked value, so view will be updated

            item.setChecked(item.itemData!!.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Timber.e(e, "Unable to get shift mode field")
    } catch (e: IllegalAccessException) {
        Timber.e(e, "Unable to change value of shift mode")
    }
}
