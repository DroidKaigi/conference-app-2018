package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.support.v4.app.Fragment
import android.util.Base64
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorBinding
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.util.CustomGlideApp

data class SponsorItem(
        val fragment: Fragment,
        val planType: SponsorPlan.Type,
        val sponsor: Sponsor
) : BindableItem<ItemSponsorBinding>(
        sponsor.hashCode().toLong()
) {
    override fun bind(viewBinding: ItemSponsorBinding, position: Int) {
        viewBinding.planType = planType

        val uri = sponsor.imageUri.uri

        when (sponsor.imageUri) {
            is Sponsor.ImageUri.Base64ImageUri -> {
                val base64encoded = uri.substring("image/png;base64,".length)
                CustomGlideApp
                        .with(fragment)
                        .load(Base64.decode(base64encoded, Base64.DEFAULT))
                        .dontAnimate()
                        .into(viewBinding.logo)
            }
            is Sponsor.ImageUri.NetworkImageUri -> {
                CustomGlideApp
                        .with(fragment)
                        .load(uri)
                        .dontAnimate()
                        .into(viewBinding.logo)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_sponsor

    override fun getSpanSize(spanCount: Int, position: Int): Int =
            getSponsorItemSpanSize(planType, spanCount)
}
