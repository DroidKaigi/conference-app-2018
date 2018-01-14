package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemDummySponsorBinding
import io.github.droidkaigi.confsched2018.model.SponsorPlan

data class EmptySponsorItem(
        val planType: SponsorPlan.Type
) : BindableItem<ItemDummySponsorBinding>(
        planType.hashCode().toLong()
), SponsorItemExt {
    override fun bind(viewBinding: ItemDummySponsorBinding, position: Int) {
        viewBinding.planType = planType
        viewBinding.constraint.adjustAspectRatioConstraint(planType, viewBinding.cover)
    }

    override fun getLayout(): Int = R.layout.item_dummy_sponsor

    override fun getSpanSize(spanCount: Int, position: Int): Int =
            getSponsorItemSpanSize(planType, spanCount)
}
