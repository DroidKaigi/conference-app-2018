package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.os.bundleOf
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentScheduleSessionsBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.pref.PreviousSessionPrefs
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment.SavePreviousSessionScroller
import io.github.droidkaigi.confsched2018.presentation.sessions.item.ScheduleSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.getScrollState
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.restoreScrollState
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import timber.log.Timber
import javax.inject.Inject

class ScheduleSessionsFragment :
        DaggerFragment(),
        OnTabReselectedListener,
        SavePreviousSessionScroller {

    private lateinit var binding: FragmentScheduleSessionsBinding

    private val sessionsSection = ScheduleSessionsSection()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm
    @Inject lateinit var sharedRecycledViewPool: RecyclerView.RecycledViewPool

    private val scheduleSessionsViewModel: ScheduleSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
                .get(ScheduleSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        scheduleSessionsViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentScheduleSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val schedule = arguments?.getSerializable(ARG_SCHEDULE) as? SessionSchedule ?: run {
            throw IllegalStateException("Start time must be given")
        }

        scheduleSessionsViewModel.schedule = schedule

        setupRecyclerView()

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        scheduleSessionsViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsSection.updateSessions(sessions, onFavoriteClickListener,
                            onFeedbackListener)
                    if (scheduleSessionsViewModel.isNeedRestoreScrollState) {
                        scrollToPreviousSession()
                    }
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        scheduleSessionsViewModel.isLoading.observe(this, { isLoading ->
            progressTimeLatch.loading = isLoading ?: false
        })
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsSection)
            setOnItemClickListener({ item, _ ->
                val sessionItem = item as? SpeechSessionItem ?: return@setOnItemClickListener
                navigationController.navigateToSessionDetailActivity(sessionItem.session)
            })
        }
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setLinearDivider(R.drawable.shape_divider_vertical_12dp,
                    layoutManager as LinearLayoutManager)
            recycledViewPool = sharedRecycledViewPool
            (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
    }

    override fun onTabReselected() {
        binding.sessionsRecycler.smoothScrollToPosition(0)
    }

    override fun requestSavingScrollState() {
        val layoutManager = binding.sessionsRecycler.layoutManager as LinearLayoutManager
        PreviousSessionPrefs.scrollState = layoutManager.getScrollState()
    }

    override fun requestRestoringScrollState() {
        if (scheduleSessionsViewModel.sessions is Result.Success<*>) {
            scrollToPreviousSession()
        } else {
            scheduleSessionsViewModel.isNeedRestoreScrollState = true
        }
    }

    private fun scrollToPreviousSession() {
        scheduleSessionsViewModel.isNeedRestoreScrollState = false
        val layoutManager = binding.sessionsRecycler.layoutManager as LinearLayoutManager
        layoutManager.restoreScrollState(PreviousSessionPrefs.scrollState)
        PreviousSessionPrefs.initPreviousSessionPrefs()
    }

    companion object {
        private const val ARG_SCHEDULE = "schedule"

        fun newInstance(schedule: SessionSchedule): ScheduleSessionsFragment =
                ScheduleSessionsFragment().apply {
                    arguments = bundleOf(ARG_SCHEDULE to schedule)
                }
    }
}
