package io.github.droidkaigi.confsched2018.presentation.about

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityAboutThisAppBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import javax.inject.Inject

class AboutThisAppActivity : BaseActivity() {
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivityAboutThisAppBinding by lazy {
        DataBindingUtil
                .setContentView<ActivityAboutThisAppBinding>(this, R.layout.activity_about_this_app)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationController.navigateToAboutThisApp()
        drawerMenu.setup(binding.drawerLayout, binding.drawer, binding.toolbar)
    }

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutThisAppActivity::class.java))
        }
    }
}
