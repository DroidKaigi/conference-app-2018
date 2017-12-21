package io.github.droidkaigi.confsched2018.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var navigationController: NavigationController

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val actionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.content_description_drawer_open, /* "open drawer" description for accessibility */
                R.string.content_description_drawer_close  /* "close drawer" description for accessibility */
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        setupBottomNavigation(savedInstanceState)
        setupNavigationDrawer()
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {

        binding.bottomNavigation.setOnNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.navigation_sessions -> {
                    navigationController.navigateToSessions()
                }
                R.id.navigation_search -> {
                    navigationController.navigateToSearch()
                }
                R.id.navigation_my_sessions -> {
                    navigationController.navigateToFavoriteSessions()
                }
                R.id.navigation_notification -> {
                    navigationController.navigateToFeed()
                }
                else -> throw NotImplementedError()
            }
            supportActionBar?.title = item.title

            true
        })
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_sessions
        }
    }

    private fun setupNavigationDrawer() {
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(
                actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.drawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item_info -> {
                }
                else -> {
                }
            }
            true
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

}
