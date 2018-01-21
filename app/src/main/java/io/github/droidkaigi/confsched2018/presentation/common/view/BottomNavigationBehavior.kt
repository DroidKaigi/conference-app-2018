package io.github.droidkaigi.confsched2018.presentation.common.view

import android.animation.Animator
import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    private var isAnimation: Boolean = false
    private var isSnackbarAppear: Boolean = false
    private var snackbar: Snackbar.SnackbarLayout? = null
    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            isAnimation = false
        }

        override fun onAnimationCancel(animation: Animator?) {
            isAnimation = false
        }

        override fun onAnimationStart(animation: Animator?) {
            isAnimation = true
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: BottomNavigationView?,
                                 dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: BottomNavigationView, directTargetChild: View,
                                     target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                dyUnconsumed: Int, type: Int) {
        when {
            dyConsumed > THRESHOLD_PX -> showBottomNavigationView(child)
            dyConsumed < THRESHOLD_PX -> hideBottomNavigationView(child)
            else -> Unit
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
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

    private fun updateSnackBarPaddingBottomByBottomNavigationView(view: BottomNavigationView) {
        snackbar?.apply {
            val translateY = (view.height - view.translationY).toInt()
            setPadding(paddingLeft, paddingTop, paddingRight, translateY)
            requestLayout()
        }
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        if (isAnimation) return
        view.animate().apply {
            translationY(0f)
            duration = DURATION_MILLIS
            setListener(animationListener)
            setUpdateListener { updateSnackBarPaddingBottomByBottomNavigationView(view) }
        }
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        if (isAnimation) return
        view.animate().apply {
            translationY(view.height.toFloat())
            duration = DURATION_MILLIS
            setListener(animationListener)
            setUpdateListener { updateSnackBarPaddingBottomByBottomNavigationView(view) }
        }
    }

    companion object {
        const val DURATION_MILLIS: Long = 150L
        const val THRESHOLD_PX: Int = 0
    }
}
