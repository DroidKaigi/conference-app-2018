package io.github.droidkaigi.confsched2018.presentation.contributor.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemContributorBinding
import io.github.droidkaigi.confsched2018.model.Contributor
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

data class ContributorItem(
        val contributor: Contributor,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemContributorBinding>(contributor.name.hashCode().toLong()) {

    override fun createViewHolder(itemView: View): ViewHolder<ItemContributorBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView, dataBindingComponent))
    }

    override fun getLayout(): Int = R.layout.item_contributor

    override fun bind(viewBinding: ItemContributorBinding, position: Int) {
        viewBinding.contributor = contributor
    }
}
