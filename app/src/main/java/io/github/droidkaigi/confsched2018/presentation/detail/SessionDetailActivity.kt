package io.github.droidkaigi.confsched2018.presentation.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivitySessionDetailBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SessionDetailActivity :
        BaseActivity(),
        SessionDetailFragment.OnClickBottomAreaListener {
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivitySessionDetailBinding by lazy {
        DataBindingUtil
                .setContentView<ActivitySessionDetailBinding>(
                        this,
                        R.layout.activity_session_detail
                )
    }

    private val sessionDetailViewModel: SessionDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SessionDetailViewModel::class.java)
    }

    private val pagerAdapter = SessionDetailFragmentPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
        sessionDetailViewModel.sessions.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    bindSessions(sessions)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        }

        binding.detailSessionsPager.adapter = pagerAdapter
        drawerMenu.setup(binding.drawerLayout, binding.drawer)
    }

    private fun bindSessions(sessions: List<Session.SpeechSession>) {
        val firstAssign = pagerAdapter.sessions.isEmpty() && sessions.isNotEmpty()
        pagerAdapter.sessions = sessions
        if (firstAssign) {
            val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
            val position = sessions.indexOfFirst { it.id == sessionId }
            binding
                    .detailSessionsPager
                    .setCurrentItem(
                            position,
                            false
                    )
        }
    }

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    override fun onClickPrevSession() {
        binding.detailSessionsPager.currentItem = binding.detailSessionsPager.currentItem - 1
    }

    override fun onClickNextSession() {
        binding.detailSessionsPager.currentItem = binding.detailSessionsPager.currentItem + 1
    }

    class SessionDetailFragmentPagerAdapter(
            fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(fragmentManager) {
        var sessions: List<Session.SpeechSession> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItem(position: Int): Fragment {
            return SessionDetailFragment.newInstance(sessions[position].id)
        }

        override fun getCount(): Int = sessions.size
    }

    companion object {
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        fun start(context: Context, session: Session) {
            context.startActivity(createIntent(context, session.id))
        }

        fun createIntent(context: Context, sessionId: String): Intent {
            return Intent(context, SessionDetailActivity::class.java).apply {
                putExtra(EXTRA_SESSION_ID, sessionId)
            }
        }
    }
}
