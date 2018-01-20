package io.github.droidkaigi.confsched2018.presentation.about.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemAboutThisAppBinding
import io.github.droidkaigi.confsched2018.model.AboutThisApp

data class AboutThisAppItem(
        val aboutThisApp: AboutThisApp
) : BindableItem<ItemAboutThisAppBinding>() {

    override fun createViewHolder(itemView: View): ViewHolder<ItemAboutThisAppBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView))
    }

    override fun getLayout(): Int = R.layout.item_about_this_app

    override fun bind(viewBinding: ItemAboutThisAppBinding, position: Int) {
        viewBinding.aboutThisApp = aboutThisApp
    }
}
