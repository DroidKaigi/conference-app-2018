@file:JvmName("SponsorItemExt")

package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.model.SponsorPlan

fun getSponsorItemSpanSize(planType: SponsorPlan.Type, spanCount: Int) = when (planType) {
    SponsorPlan.Type.Platinum -> spanCount / 2
    SponsorPlan.Type.Gold,
    SponsorPlan.Type.Silver,
    SponsorPlan.Type.TechnicalForNetwork,
    SponsorPlan.Type.Supporter -> spanCount / 3
}

@ColorInt
fun fromPlanType(context: Context, planType: SponsorPlan.Type?) =
        ContextCompat.getColor(context, when (planType) {
            SponsorPlan.Type.Platinum -> R.color.sponsor_platinum
            SponsorPlan.Type.Gold -> R.color.sponsor_gold
            SponsorPlan.Type.Silver -> R.color.sponsor_silver
            SponsorPlan.Type.Supporter -> R.color.sponsor_supporter
            SponsorPlan.Type.TechnicalForNetwork -> R.color.sponsor_technical_for_network
            else -> R.color.sponsor_supporter
        })

fun toDimensionRatio(planType: SponsorPlan.Type?) = when (planType) {
    SponsorPlan.Type.Platinum,
    SponsorPlan.Type.Supporter -> "H,16:9"
    SponsorPlan.Type.Gold -> "H,1:1"
    SponsorPlan.Type.Silver,
    SponsorPlan.Type.TechnicalForNetwork -> "H,4:3"
    else -> "H,1:1"
}
