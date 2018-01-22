package io.github.droidkaigi.confsched2018.presentation

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityMainBinding
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.util.ext.disableShiftMode
import io.github.droidkaigi.confsched2018.util.ext.elevationForPostLollipop
import timber.log.Timber
import javax.inject.Inject



class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        setupBottomNavigation(savedInstanceState)
        drawerMenu.setup(binding.toolbar, binding.drawerLayout, binding.drawer, true)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        binding.bottomNavigation.disableShiftMode()
        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener({ item ->
            val navigationItem = BottomNavigationItem
                    .values()
                    .first { it.menuId == item.itemId }

            binding.toolbar.elevationForPostLollipop = if (navigationItem.isUseToolbarElevation) {
                resources.getDimensionPixelSize(R.dimen.elevation_app_bar).toFloat()
            } else {
                0F
            }
            supportActionBar?.apply {
                title = if (navigationItem.imageRes != null) {
                    setDisplayShowHomeEnabled(true)
                    setIcon(navigationItem.imageRes)
                    null
                } else {
                    setDisplayShowHomeEnabled(false)
                    setIcon(null)
                    item.title
                }
            }

            navigationItem.navigate(navigationController)
            true
        })
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_sessions
        }
        binding.bottomNavigation.setOnNavigationItemReselectedListener { }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val navigationItem: BottomNavigationItem =
                    BottomNavigationItem.forId(binding.bottomNavigation.selectedItemId)
            Timber.d("menu: %s", navigationItem)
            supportActionBar?.apply {
                title = if (navigationItem.imageRes != null) {
                    setDisplayShowHomeEnabled(true)
                    setIcon(navigationItem.imageRes)
                    null
                } else {
                    setDisplayShowHomeEnabled(false)
                    setIcon(null)
                    getString(navigationItem.titleRes!!)
                }
            }
        }
    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    enum class BottomNavigationItem(
            @MenuRes val menuId: Int,
            @StringRes val titleRes: Int?,
            @DrawableRes val imageRes: Int?,
            val isUseToolbarElevation: Boolean,
            val navigate: NavigationController.() -> Unit
    ) {
        SESSION(R.id.navigation_sessions,null,  R.drawable.ic_logo_white, false, {
            navigateToSessions()
        }),
        SEARCH(R.id.navigation_search, R.string.search_title, null, false, {
            navigateToSearch()
        }),
        FAVORITE(R.id.navigation_favorite_sessions, R.string.favorite_title, null, true, {
            navigateToFavoriteSessions()
        }),
        FEED(R.id.navigation_feed, R.string.feed_title,null, true, {
            navigateToFeed()
        });

        companion object {
            fun forId(@IdRes id: Int): BottomNavigationItem {
                values().forEach {
                    if (id == it.menuId)
                        return it
                }
                throw IllegalArgumentException("wtf!")
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
