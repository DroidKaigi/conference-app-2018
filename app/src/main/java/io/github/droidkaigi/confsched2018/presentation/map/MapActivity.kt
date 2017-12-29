package io.github.droidkaigi.confsched2018.presentation.map

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityMapBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import javax.inject.Inject

class MapActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var navigationController: NavigationController

    private val binding: ActivityMapBinding by lazy {
        DataBindingUtil.setContentView<ActivityMapBinding>(this, R.layout.activity_map)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        navigationController.navigateToMap()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MapActivity::class.java))
        }
    }
}
