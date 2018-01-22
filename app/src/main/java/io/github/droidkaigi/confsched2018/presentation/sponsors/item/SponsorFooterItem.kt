package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorFooterBinding
import io.github.droidkaigi.confsched2018.model.SponsorPlan

data class SponsorFooterItem(
        val planType: SponsorPlan.Type
) : BindableItem<ItemSponsorFooterBinding>(
        planType.hashCode().toLong()
) {
    override fun bind(viewBinding: ItemSponsorFooterBinding, position: Int) {
        viewBinding.planType = planType
    }

    override fun getLayout(): Int = R.layout.item_sponsor_footer
}
