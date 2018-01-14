package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorBinding
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.context

data class SponsorItem(
        val planType: SponsorPlan.Type,
        val sponsor: Sponsor
) : BindableItem<ItemSponsorBinding>(
        sponsor.hashCode().toLong()
) {

    override fun bind(viewBinding: ItemSponsorBinding, position: Int) {
//        viewBinding.card.adjustAspectRatio()
        viewBinding.root.findViewById<ConstraintLayout>(R.id.constraint).adjustAspectRatioConstraint(viewBinding.card)

        // FIXME into Databinding adapters
        when (sponsor.imageUri) {
            is Sponsor.ImageUri.Base64ImageUri -> {
                // FIXME base64
            }
            is Sponsor.ImageUri.NetworkImageUri -> {
                CustomGlideApp
                        .with(viewBinding.context)
                        .load(sponsor.imageUri.uri)
                        .dontAnimate()
                        .into(viewBinding.logo)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_sponsor

    override fun getSpanSize(spanCount: Int, position: Int): Int = when (planType) {
        SponsorPlan.Type.Platinum, SponsorPlan.Type.TechnicalForNetwork -> spanCount / 2
        SponsorPlan.Type.Gold, SponsorPlan.Type.Silver, SponsorPlan.Type.Supporter -> spanCount / 3
    }

    private fun ConstraintLayout.adjustAspectRatioConstraint(target: View) {
        val lp = target.layoutParams as? ConstraintLayout.LayoutParams ?: throw IllegalStateException("layout structure was changed.")
        val newDimensionRatio = planType.toDimensionRatio()

        if (newDimensionRatio == lp.dimensionRatio) {
            return
        }

        val newConstraint = ConstraintSet().apply { clone(this@adjustAspectRatioConstraint) }
        newConstraint.setDimensionRatio(target.id, newDimensionRatio)
        newConstraint.applyTo(this)
    }

    private fun SponsorPlan.Type.toDimensionRatio() = when (this) {
        SponsorPlan.Type.Platinum, SponsorPlan.Type.Supporter, SponsorPlan.Type.TechnicalForNetwork -> "H,16:9"
        SponsorPlan.Type.Gold -> "H,1:1"
        SponsorPlan.Type.Silver -> "H,4:3"
    }
}
