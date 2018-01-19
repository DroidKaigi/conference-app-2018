package io.github.droidkaigi.confsched2018.presentation.contributor.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemAboutThisAppHeaderBinding
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent

class AboutThisAppHeaderItem(
        private val headItem: AboutThisApp.HeadItem,
        private val onAboutThisHeaderIconClickListener: (String) -> Unit?,
        private val dataBindingComponent: FragmentDataBindingComponent
) : BindableItem<ItemAboutThisAppHeaderBinding>() {

    override fun createViewHolder(itemView: View): ViewHolder<ItemAboutThisAppHeaderBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView, dataBindingComponent))
    }

    override fun getLayout(): Int = R.layout.item_about_this_app_header

    override fun bind(viewBinding: ItemAboutThisAppHeaderBinding, position: Int) {
        viewBinding.headItem = headItem
        viewBinding.aboutThisAppFacebook.setOnClickListener {
            onAboutThisHeaderIconClickListener(headItem.faceBookUrl)
        }
        viewBinding.aboutThisAppTwitter.setOnClickListener {
            onAboutThisHeaderIconClickListener(headItem.twitterUrl)
        }
        viewBinding.aboutThisAppGithub.setOnClickListener {
            onAboutThisHeaderIconClickListener(headItem.githubUrl)
        }
        viewBinding.aboutThisAppYoutube.setOnClickListener {
            onAboutThisHeaderIconClickListener(headItem.youtubeUrl)
        }
    }

    override fun isSameAs(other: Item<*>?): Boolean =
            other is AboutThisAppHeaderItem

    override fun equals(other: Any?): Boolean =
            headItem == (other as? AboutThisAppHeaderItem?)?.headItem

    override fun hashCode(): Int = headItem.hashCode()
}
