package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * A custom view for showing move next or prev session
 *
 * ref: https://github.com/DroidKaigi/conference-app-2018/issues/180
 */

class BottomBarBehavior(context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<ConstraintLayout>(context, attrs) {

    private var defaultDependencyTop = -1

    override fun layoutDependsOn(parent: CoordinatorLayout?, bottomBar: ConstraintLayout?,
                                 dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, bottomBar: ConstraintLayout,
                                        dependency: View): Boolean {
        if (defaultDependencyTop == -1) {
            defaultDependencyTop = dependency.top
        }
        bottomBar.translationY = (-dependency.top + defaultDependencyTop).toFloat()
        return true
    }
}
