package io.github.droidkaigi.confsched2018.presentation.common.menu

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject

class DrawerMenu @Inject constructor(
        private val activity: AppCompatActivity,
        private val navigationController: NavigationController
) {
    private lateinit var drawerLayout: DrawerLayout

    fun setup(
            toolbar: Toolbar,
            drawerLayout: DrawerLayout,
            navigationView: NavigationView,
            actionBarDrawerSync: Boolean = false
    ) {
        this.drawerLayout = drawerLayout
        if (actionBarDrawerSync) {
            ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolbar,
                    R.string.nav_content_description_drawer_open,
                    R.string.nav_content_description_drawer_close
            ).also {
                drawerLayout.addDrawerListener(it)
            }.apply {
                isDrawerIndicatorEnabled = true
                isDrawerSlideAnimationEnabled = true
                syncState()
            }
        }
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item_map -> navigationController.navigateToMapActivity()
                R.id.nav_item_setting -> navigationController.navigateToSettingsActivity()
                R.id.nav_item_sponsor -> navigationController.navigateToSponsorsActivity()
                R.id.nav_item_info -> navigationController.navigateToAboutThisAppActivity()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    fun closeDrawerIfNeeded(): Boolean {
        return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
            false
        } else {
            true
        }
    }
}
