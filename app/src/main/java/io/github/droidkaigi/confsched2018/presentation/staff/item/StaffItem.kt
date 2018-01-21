package io.github.droidkaigi.confsched2018.presentation.staff.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemStaffBinding
import io.github.droidkaigi.confsched2018.model.Staff

data class StaffItem(
        val staff: Staff
) : BindableItem<ItemStaffBinding>(staff.name.hashCode().toLong()) {

    override fun createViewHolder(itemView: View): ViewHolder<ItemStaffBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView))
    }

    override fun getLayout(): Int = R.layout.item_staff

    override fun bind(viewBinding: ItemStaffBinding, position: Int) {
        viewBinding.staff = staff
    }
}
