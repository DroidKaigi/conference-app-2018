package io.github.droidkaigi.confsched2018.presentation.speaker

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivitySpeakerDetailBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import javax.inject.Inject

class SpeakerDetailActivity : DaggerAppCompatActivity() {
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivitySpeakerDetailBinding by lazy {
        DataBindingUtil
                .setContentView<ActivitySpeakerDetailBinding>(
                        this,
                        R.layout.activity_speaker_detail
                )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportPostponeEnterTransition()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        if (savedInstanceState == null) {
            navigationController.navigateToSpeakerDetail(
                    intent.getStringExtra(EXTRA_SPEAKER_ID),
                    intent.getStringExtra(EXTRA_TRANSITION_NAME))
        }
        drawerMenu.setup(binding.drawerLayout, binding.drawer, binding.toolbar)
    }

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID"
        const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"

        fun start(context: Context, speakerId: String) {
            context.startActivity(Intent(context, SpeakerDetailActivity::class.java).apply {
                putExtra(EXTRA_SPEAKER_ID, speakerId)
            })
        }

        fun start(activity: AppCompatActivity,
                  sharedElement: Pair<View, String>,
                  speakerId: String) {
            val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement)
            activity.startActivity(Intent(activity, SpeakerDetailActivity::class.java).apply {
                putExtra(EXTRA_SPEAKER_ID, speakerId)
                putExtra(EXTRA_TRANSITION_NAME, sharedElement.second)
            }, options.toBundle())
        }
    }
}
