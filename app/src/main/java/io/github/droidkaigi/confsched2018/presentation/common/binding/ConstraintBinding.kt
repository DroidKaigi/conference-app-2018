package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.BindingAdapter
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View

@BindingAdapter("layout_constraintDimensionRatio")
fun View.setConstraintDimensionRatio(newDimensionRatio: String) {
    val id = id.takeIf { it != View.NO_ID } ?: return
    val parent = parent as? ConstraintLayout ?: return
    val lp = layoutParams as? ConstraintLayout.LayoutParams ?: return

    if (newDimensionRatio == lp.dimensionRatio) {
        return
    }

    ConstraintSet().apply {
        clone(parent)
        setDimensionRatio(id, newDimensionRatio)
    }.applyTo(parent)
}
