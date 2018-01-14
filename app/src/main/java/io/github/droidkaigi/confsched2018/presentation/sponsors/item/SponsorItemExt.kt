package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.content.Context
import android.support.annotation.ColorInt
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.view.View
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.SponsorPlan

fun getSponsorItemSpanSize(planType: SponsorPlan.Type, spanCount: Int) = when (planType) {
    SponsorPlan.Type.Platinum,
    SponsorPlan.Type.TechnicalForNetwork -> spanCount / 2
    SponsorPlan.Type.Gold,
    SponsorPlan.Type.Silver,
    SponsorPlan.Type.Supporter -> spanCount / 3
}

object SponsorColor {
    @ColorInt
    @JvmStatic // to use in DataBinding
    fun fromPlanType(context: Context, planType: SponsorPlan.Type?) =
            ContextCompat.getColor(context, when (planType) {
                SponsorPlan.Type.Platinum -> R.color.sponsor_platinum
                SponsorPlan.Type.Gold -> R.color.sponsor_gold
                SponsorPlan.Type.Silver -> R.color.sponsor_silver
                SponsorPlan.Type.Supporter -> R.color.sponsor_supporter
                SponsorPlan.Type.TechnicalForNetwork -> R.color.sponsor_technical_for_network
                else -> R.color.sponsor_supporter
            })
}

interface SponsorItemExt {
    fun SponsorPlan.Type.toDimensionRatio() = when (this) {
        SponsorPlan.Type.Platinum,
        SponsorPlan.Type.Supporter,
        SponsorPlan.Type.TechnicalForNetwork -> "H,16:9"
        SponsorPlan.Type.Gold -> "H,1:1"
        SponsorPlan.Type.Silver -> "H,4:3"
    }

    fun ConstraintLayout.adjustAspectRatioConstraint(planType: SponsorPlan.Type, target: View) {
        val lp = target.layoutParams as? ConstraintLayout.LayoutParams ?: let {
            throw IllegalStateException("layout structure was changed.")
        }

        val newDimensionRatio = planType.toDimensionRatio()

        if (newDimensionRatio == lp.dimensionRatio) {
            return
        }

        ConstraintSet().apply {
            clone(this@adjustAspectRatioConstraint)
            setDimensionRatio(target.id, newDimensionRatio)
        }.applyTo(this)
    }
}
