package io.github.droidkaigi.confsched2018.presentation.contributor.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemContributorHeaderBinding
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class ContributorHeaderItem(
        private val contributorCount: Int,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemContributorHeaderBinding>() {

    override fun createViewHolder(itemView: View): ViewHolder<ItemContributorHeaderBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView, dataBindingComponent))
    }

    override fun getLayout(): Int = R.layout.item_contributor_header

    override fun bind(viewBinding: ItemContributorHeaderBinding, position: Int) {
        viewBinding.contributorCount = contributorCount
    }

    override fun isSameAs(other: Item<*>?): Boolean =
            other is ContributorHeaderItem

    override fun equals(other: Any?): Boolean =
            contributorCount == (other as? ContributorHeaderItem?)?.contributorCount

    override fun hashCode(): Int = contributorCount
}
