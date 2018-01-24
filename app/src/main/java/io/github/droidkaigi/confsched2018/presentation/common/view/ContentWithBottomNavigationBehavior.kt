package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * This behavior specified for ViewGroup used in activity_main.xml id=content.
 * Be carefully to use other ViewGroup.
 */
class ContentWithBottomNavigationBehavior(context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<ViewGroup>(context, attrs) {
    private var previousHeight: Float = 0f
    private var requestResetBnv: Boolean = false

    override fun onMeasureChild(parent: CoordinatorLayout, child: ViewGroup,
                                parentWidthMeasureSpec: Int, widthUsed: Int,
                                parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        val bnv: BottomNavigationView = parent.getDependencies(child)
                .firstOrNull { it is BottomNavigationView } as? BottomNavigationView
        // this behavior specified for ViewGroup used with BottomNavigationView
                ?: return super.onMeasureChild(parent, child, parentWidthMeasureSpec,
                        widthUsed, parentHeightMeasureSpec, heightUsed)

        // avoid the content is overlapped by BottomNavigationView
        // compute content area size to fits to BNV upper line

        // compute CoordinatorLayout height
        val parentMeasureSpecHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
        val availableHeight =
                if (parentMeasureSpecHeight == 0) parent.height else parentMeasureSpecHeight

        // compute BottomNavigationView height
        // translationY == 0 means BNV perfectly showed so content area fits to BNV upper line
        // otherwise BNV is going to hide and content area fits to CoordinatorLayout bottom line
        // if use BNV's just height we cannot scroll smoothly.
        val bnvHeight = if (bnv.translationY == 0f) bnv.height else 0

        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(availableHeight - bnvHeight,
                View.MeasureSpec.EXACTLY)
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed)
        return true
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: ViewGroup?,
                                 dependency: View?): Boolean {
        return dependency is BottomNavigationView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: ViewGroup?,
                                        dependency: View?): Boolean {
        if (dependency is BottomNavigationView) {
            val height = dependency.height - dependency.translationY
            if (previousHeight != height) {
                child?.requestLayout()
                previousHeight = height
            }
            return true
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}
