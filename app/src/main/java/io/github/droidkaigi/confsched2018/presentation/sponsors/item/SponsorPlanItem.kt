package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import com.google.android.flexbox.FlexboxLayoutManager
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorPlanBinding
import io.github.droidkaigi.confsched2018.model.SponsorPlan

data class SponsorPlanItem(
        val sponsorPlan: SponsorPlan
) : BindableItem<ItemSponsorPlanBinding>(
        sponsorPlan.hashCode().toLong()
) {

    override fun bind(viewBinding: ItemSponsorPlanBinding, position: Int) {
        val lp = viewBinding.root.layoutParams as? FlexboxLayoutManager.LayoutParams ?: throw AssertionError("LayoutManager was changed")

        lp.flexBasisPercent = 1f
        lp.isWrapBefore = true

        viewBinding.name = sponsorPlan.name
    }

    override fun getLayout(): Int = R.layout.item_sponsor_plan
}
