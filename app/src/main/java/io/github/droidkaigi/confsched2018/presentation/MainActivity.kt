package io.github.droidkaigi.confsched2018.presentation

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityMainBinding
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs
import io.github.droidkaigi.confsched2018.presentation.common.view.BottomNavigationBehavior
import io.github.droidkaigi.confsched2018.presentation.common.view.BottomNavigationHideBehavior
import io.github.droidkaigi.confsched2018.util.ext.disableShiftMode
import io.github.droidkaigi.confsched2018.util.ext.elevationForPostLollipop
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
        drawerMenu.setup(binding.drawerLayout, binding.drawer, binding.toolbar, true)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        (binding.bottomNavigation.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                if (Prefs.enableHideBottomNavigationBar) {
                    BottomNavigationHideBehavior()
                } else {
                    BottomNavigationBehavior()
                }
        binding.bottomNavigation.disableShiftMode()
        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener({ item ->
            val navigationItem = BottomNavigationItem
                    .forId(item.itemId)

            setupToolbar(navigationItem)

            navigationItem.navigate(navigationController)
            true
        })
        if (savedInstanceState == null) {
            when (intent.getStringExtra("shortcut")) {
                "favorite" -> {
                    binding.bottomNavigation.selectedItemId = R.id.navigation_favorite_sessions
                }
                else -> binding.bottomNavigation.selectedItemId = R.id.navigation_sessions
            }
        }
        binding.bottomNavigation.setOnNavigationItemReselectedListener { item ->
            val navigationItem = BottomNavigationItem
                    .forId(item.itemId)
            val fragment = supportFragmentManager.findFragmentByTag(navigationItem.name)
            if (fragment is BottomNavigationItem.OnReselectedListener) {
                fragment.onReselected()
            }
        }
    }

    private fun setupToolbar(navigationItem: BottomNavigationItem) {
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
                getString(navigationItem.titleRes!!)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupToolbar(BottomNavigationItem.forId(binding.bottomNavigation.selectedItemId))
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
        SESSION(R.id.navigation_sessions, null, R.drawable.ic_logo_white, false, {
            navigateToSessions()
        }),
        SEARCH(R.id.navigation_search, R.string.search_title, null, false, {
            navigateToSearch()
        }),
        FAVORITE(R.id.navigation_favorite_sessions, R.string.favorite_title, null, true, {
            navigateToFavoriteSessions()
        }),
        FEED(R.id.navigation_feed, R.string.feed_title, null, true, {
            navigateToFeed()
        });

        interface OnReselectedListener {
            fun onReselected()
        }

        companion object {
            fun forId(@IdRes id: Int): BottomNavigationItem {
                return values().first { it.menuId == id }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

        fun start(context: Context) {
            createIntent(context).let {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        }
    }
}
