package io.github.droidkaigi.confsched2018.presentation.common.menu

import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.View
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.MainActivity
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject
import kotlin.reflect.KClass

class DrawerMenu @Inject constructor(
        private val activity: AppCompatActivity,
        private val navigationController: NavigationController
) {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var currentNavigationItem: DrawerNavigationItem

    fun setup(
            drawerLayout: DrawerLayout,
            navigationView: NavigationView,
            toolbar: Toolbar? = null,
            actionBarDrawerSync: Boolean = false
    ) {
        this.drawerLayout = drawerLayout
        if (actionBarDrawerSync) {
            object : ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolbar,
                    R.string.nav_content_description_drawer_open,
                    R.string.nav_content_description_drawer_close
            ) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    if (activity.currentFocus is SearchView.SearchAutoComplete) {
                        drawerView.requestFocus()
                    }
                }
            }.also {
                drawerLayout.addDrawerListener(it)
            }.apply {
                isDrawerIndicatorEnabled = true
                isDrawerSlideAnimationEnabled = false
                syncState()
            }
        }
        navigationView.setNavigationItemSelectedListener { item ->
            DrawerNavigationItem
                    .values()
                    .firstOrNull { it.menuId == item.itemId }
                    ?.apply {
                        if (this != currentNavigationItem) {
                            navigate(navigationController)
                        }
                    }
            drawerLayout.closeDrawers()
            false
        }

        currentNavigationItem = DrawerNavigationItem
                .values()
                .firstOrNull { activity::class == it.activityClass }
                ?.also {
                    navigationView.setCheckedItem(it.menuId)
                }
                ?: DrawerNavigationItem.OTHER
    }

    fun closeDrawerIfNeeded(): Boolean {
        return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
            false
        } else {
            true
        }
    }

    enum class DrawerNavigationItem(
            @IdRes val menuId: Int,
            val activityClass: KClass<*>,
            val navigate: NavigationController.() -> Unit
    ) {
        HOME(R.id.nav_item_home, MainActivity::class, {
            navigateToMainActivity()
        }),
        FEEDBACK(R.id.nav_item_all_feedback, Unit::class, {
            navigateToExternalBrowser("https://goo.gl/forms/Hjp54vk5P0VILcgf1")
        }),
        OTHER(0, Unit::class, {
            //do nothing
        })
    }
}
