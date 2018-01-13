package io.github.droidkaigi.confsched2018.presentation.sponsors.item

import android.webkit.URLUtil
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemSponsorBinding
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.context

data class SponsorItem(
        val sponsor: Sponsor
) : BindableItem<ItemSponsorBinding>(
        sponsor.hashCode().toLong()
) {

    override fun bind(viewBinding: ItemSponsorBinding, position: Int) {
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
}
