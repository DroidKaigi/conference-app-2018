package io.github.droidkaigi.confsched2018.presentation.contributor.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemAboutThisAppBinding
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

data class AboutThisAppItem(
        val aboutThisApp: AboutThisApp,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemAboutThisAppBinding>() {

    override fun createViewHolder(itemView: View): ViewHolder<ItemAboutThisAppBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView, dataBindingComponent))
    }

    override fun getLayout(): Int = R.layout.item_about_this_app

    override fun bind(viewBinding: ItemAboutThisAppBinding, position: Int) {
        viewBinding.aboutThisApp = aboutThisApp
    }
}
