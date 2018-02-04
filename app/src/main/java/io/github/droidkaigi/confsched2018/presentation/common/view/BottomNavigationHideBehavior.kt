package io.github.droidkaigi.confsched2018.presentation.common.view

import android.animation.Animator
import android.content.Context
import android.support.annotation.Keep
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Behavior to hide BottomNavigationView
 */
class BottomNavigationHideBehavior : BottomNavigationBehavior {
    @Keep constructor() : super()
    @Keep constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var isAnimation: Boolean = false
    private var isFlinging: Boolean = false
    private var needsShowBnv: Boolean = false
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

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: BottomNavigationView, directTargetChild: View,
                                     target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                dyUnconsumed: Int, type: Int) {
        // skip scroll event via fling
        if (isFlinging && type == ViewCompat.TYPE_NON_TOUCH) return
        when {
        // positive value: finger's move = touch -> move up (contents are scrolled downward)
            dyConsumed > THRESHOLD_PX -> hideBottomNavigationView(child)
        // negative value: finger's move = touch -> move down (contents are scrolled upward)
            dyConsumed < THRESHOLD_PX -> showBottomNavigationView(child)
        // ignore case: user scrolls contents upward but contents are too short to scroll
            dyConsumed == 0 && dyUnconsumed < 0 -> needsShowBnv = true
            else -> Unit
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                               target: View, velocityX: Float, velocityY: Float,
                               consumed: Boolean): Boolean {
        isFlinging = true
        when {
            velocityY > 0 -> hideBottomNavigationView(child)
            velocityY < 0 -> showBottomNavigationView(child)
            else -> Unit
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout,
                                    child: BottomNavigationView, target: View, type: Int) {
        if (type == ViewCompat.TYPE_NON_TOUCH) isFlinging = false
        if (needsShowBnv) {
            // user touches screen but contents are not scrolled
            // this happens like contents are shot enough which has no reason to scroll
            // on the situation, it is necessary for the user to show BNV
            showBottomNavigationView(child)
            needsShowBnv = false
        }
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        if (isAnimation) return
        view.animate().apply {
            translationY(0f)
            duration = DURATION_MILLIS
            setListener(animationListener)
            setUpdateListener { updateSnackBarPaddingBottomByBottomNavigationView(view) }
        }
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
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
