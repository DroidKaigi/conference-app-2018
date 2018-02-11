package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.annotation.Keep
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import io.github.droidkaigi.confsched2018.util.ext.setPaddingWithLayoutDirection

/**
 * Behavior to show Snackbar above BottomNavigationView
 */
open class BottomNavigationBehavior : CoordinatorLayout.Behavior<BottomNavigationView> {

    // for set behavior on programmatically
    @Keep constructor() : super()

    // for set behavior on XML
    @Keep constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var isSnackbarAppear: Boolean = false
    private var snackbar: Snackbar.SnackbarLayout? = null

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: BottomNavigationView?,
                                 dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: BottomNavigationView?,
                                        dependency: View?): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            if (isSnackbarAppear) return true
            isSnackbarAppear = true
            snackbar = dependency
            child?.let { updateSnackBarPaddingBottomByBottomNavigationView(it) }
            return true
        }
        return false
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: BottomNavigationView?,
                                        dependency: View?) {
        when (dependency) {
            is Snackbar.SnackbarLayout -> {
                isSnackbarAppear = false
                snackbar = null
            }
            else -> super.onDependentViewRemoved(parent, child, dependency)
        }
    }

    internal fun updateSnackBarPaddingBottomByBottomNavigationView(view: BottomNavigationView) {
        snackbar?.apply {
            val translateY = (view.height - view.translationY).toInt()
            setPaddingWithLayoutDirection(paddingStart, paddingTop, paddingEnd, translateY)
            requestLayout()
        }
    }
}
