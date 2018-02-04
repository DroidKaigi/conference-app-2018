package io.github.droidkaigi.confsched2018.presentation.common.view

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager

class OnTabReselectedListener(private val viewPager: ViewPager) : TabLayout.OnTabSelectedListener {

    override fun onTabUnselected(tab: TabLayout.Tab) {
        // no-op
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        // no-op
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        val adapter = viewPager.adapter as FragmentStatePagerAdapter
        val item = adapter.getItem(tab.position)
        if (item is TabLayoutItem) {
            item.scrollToTop()
        }
    }
}

interface TabLayoutItem {
    fun scrollToTop()
}
