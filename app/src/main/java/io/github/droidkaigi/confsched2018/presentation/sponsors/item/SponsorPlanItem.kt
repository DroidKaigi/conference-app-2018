package io.github.droidkaigi.confsched2018.presentation.sponsors.item

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
        viewBinding.name = sponsorPlan.name
    }

    override fun getLayout(): Int = R.layout.item_sponsor_plan
}
