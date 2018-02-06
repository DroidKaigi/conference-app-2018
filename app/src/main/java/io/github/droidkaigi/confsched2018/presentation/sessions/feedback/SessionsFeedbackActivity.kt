package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivitySessionsFeedbackBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SessionsFeedbackActivity : BaseActivity() {

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var drawerMenu: DrawerMenu

    private val sessionsFeedbackViewModel: SessionsFeedbackViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SessionsFeedbackViewModel::class.java)
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding: ActivitySessionsFeedbackBinding by lazy {
        DataBindingUtil.setContentView<ActivitySessionsFeedbackBinding>(this,
                R.layout.activity_sessions_feedback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sessionsFeedbackViewModel.sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
        sessionsFeedbackViewModel.session.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    supportActionBar?.title = result.data.title
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })

        navigationController.navigateToFeedback()
        drawerMenu.setup(binding.drawerLayout, binding.drawer, binding.toolbar)
    }

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        fun start(context: Context, session: Session.SpeechSession) {
            context.startActivity(Intent(context, SessionsFeedbackActivity::class.java).apply {
                putExtra(EXTRA_SESSION_ID, session.id)
            })
        }
    }
}
