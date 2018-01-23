package io.github.droidkaigi.confsched2018.presentation.staff.item

import android.databinding.DataBindingUtil
import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemStaffHeaderBinding

class StaffHeaderItem(
        private val staffCount: Int
) : BindableItem<ItemStaffHeaderBinding>() {

    override fun createViewHolder(itemView: View): ViewHolder<ItemStaffHeaderBinding> {
        return ViewHolder(DataBindingUtil.bind(itemView))
    }

    override fun getLayout(): Int = R.layout.item_staff_header

    override fun bind(viewBinding: ItemStaffHeaderBinding, position: Int) {
        viewBinding.staffCount = staffCount
    }

    override fun isSameAs(other: Item<*>?): Boolean =
            other is StaffHeaderItem

    override fun equals(other: Any?): Boolean =
            staffCount == (other as? StaffHeaderItem?)?.staffCount

    override fun hashCode(): Int = staffCount
}
