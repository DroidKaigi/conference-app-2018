package io.github.droidkaigi.confsched2018.presentation.common.view

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

class OnTabReselectedDispatcher(
        private val viewPager: ViewPager
) : TabLayout.OnTabSelectedListener {

    override fun onTabUnselected(tab: TabLayout.Tab) {
        // no-op
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        // no-op
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        val item = viewPager.adapter?.instantiateItem(viewPager, tab.position)
        if (item is OnTabReselectedListener) {
            item.onTabReselected()
        }
    }
}

interface OnTabReselectedListener {
    fun onTabReselected()
}
