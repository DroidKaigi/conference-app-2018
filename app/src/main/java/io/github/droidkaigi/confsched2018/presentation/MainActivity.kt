package io.github.droidkaigi.confsched2018.presentation

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
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
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        setupBottomNavigation(savedInstanceState)
        setupNavigationDrawer()
    }

    @SuppressLint("RestrictedApi")
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
                }

                R.id.navigation_notification -> {
                }
                else -> throw NotImplementedError()
            }
            true
        })
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_sessions
        }

        // for disable shifting mode
        val bottomNavigationMenuView: ViewGroup = binding.bottomNavigation.getChildAt(0) as ViewGroup

        bottomNavigationMenuView.javaClass
                .declaredFields
                .filter { it.name == "mShiftingMode" }
                .map {
                    it.isAccessible = true
                    it.setBoolean(bottomNavigationMenuView, false)
                }

        (0 until bottomNavigationMenuView.childCount)
                .mapNotNull { bottomNavigationMenuView.getChildAt(it) as? BottomNavigationItemView }
                .forEach {
                    it.apply {
                        setShiftingMode(false)
                        setChecked(false)
                    }
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
