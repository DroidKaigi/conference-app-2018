package io.github.droidkaigi.confsched2018.presentation.common.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import dagger.android.support.DaggerAppCompatActivity
import io.github.droidkaigi.confsched2018.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTaskDescription()
    }

    private fun initTaskDescription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(ActivityManager.TaskDescription(
                    null, null, ContextCompat.getColor(this, R.color.recents_background))
            )
        }
    }
}
