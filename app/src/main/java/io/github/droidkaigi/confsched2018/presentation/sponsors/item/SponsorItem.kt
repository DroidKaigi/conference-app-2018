package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.webkit.URLUtil
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorBinding
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.sponsors.SPONSOR_MAX_SPAN_SIZE
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.context

data class SponsorItem(
        val planType: SponsorPlan.Type,
        val sponsor: Sponsor
) : BindableItem<ItemSponsorBinding>(
        sponsor.hashCode().toLong()
), SpanSizeProvidable {

    override fun bind(viewBinding: ItemSponsorBinding, position: Int) {
//        viewBinding.card.adjustAspectRatio()
        viewBinding.root.findViewById<ConstraintLayout>(R.id.constraint).adjustAspectRatioConstraint(viewBinding.card)

        // FIXME into Databinding adapters
        if (URLUtil.isNetworkUrl(sponsor.imageUri.uri)) {
            CustomGlideApp
                    .with(viewBinding.context)
                    .load(sponsor.imageUri.uri)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .dontAnimate()
                    .into(viewBinding.logo)
        } else {
            // base64
        }
    }

    override fun getLayout(): Int = R.layout.item_sponsor

    override fun getSpanSize(): Int = when (planType) {
        SponsorPlan.Type.Plutinum, SponsorPlan.Type.TechnicalForNetwork -> SPONSOR_MAX_SPAN_SIZE / 2
        SponsorPlan.Type.Gold, SponsorPlan.Type.Silver, SponsorPlan.Type.Supporter -> SPONSOR_MAX_SPAN_SIZE / 3
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
        SponsorPlan.Type.Plutinum -> "H,16:9"
        SponsorPlan.Type.Gold -> "H,1:1"
        SponsorPlan.Type.Silver -> "H,4:3"
        SponsorPlan.Type.Supporter -> "H,16:9"
        SponsorPlan.Type.TechnicalForNetwork -> "H,16:9"
    }
}
