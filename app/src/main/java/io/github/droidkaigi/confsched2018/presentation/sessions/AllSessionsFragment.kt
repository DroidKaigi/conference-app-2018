package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentAllSessionsBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.pref.PreviousSessionPrefs
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment.CurrentSessionScroller
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionsFragment.SavePreviousSessionScroller
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.addOnScrollListener
import io.github.droidkaigi.confsched2018.util.ext.getScrollState
import io.github.droidkaigi.confsched2018.util.ext.isGone
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.restoreScrollState
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import io.github.droidkaigi.confsched2018.util.ext.setTextIfChanged
import io.github.droidkaigi.confsched2018.util.ext.setVisible
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class AllSessionsFragment :
        DaggerFragment(),
        CurrentSessionScroller,
        OnTabReselectedListener,
        SavePreviousSessionScroller {

    private lateinit var binding: FragmentAllSessionsBinding

    private val sessionsSection = DateSessionsSection()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm
    @Inject lateinit var sharedRecycledViewPool: RecyclerView.RecycledViewPool

    private val allSessionsViewModel: AllSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AllSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        allSessionsViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAllSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        allSessionsViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsSection.updateSessions(sessions, onFavoriteClickListener,
                            onFeedbackListener, true)

                    allSessionsViewModel.onShowSessions()
                    if (allSessionsViewModel.isNeedRestoreScrollState) {
                        scrollToPreviousSession()
                    }
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        allSessionsViewModel.isLoading.observe(this, { isLoading ->
            progressTimeLatch.loading = isLoading ?: false
        })
        allSessionsViewModel.refreshFocusCurrentSession.observe(this, {
            if (it != true) return@observe
            scrollToCurrentSession()
        })
    }

    override fun scrollToCurrentSession() {
        val now = Date(ZonedDateTime.now(ZoneId.of(ZoneId.SHORT_IDS["JST"]))
                .toInstant().toEpochMilli())
        val currentSessionPosition = sessionsSection.getDateHeaderPositionByDate(now)
        binding.sessionsRecycler.scrollToPosition(currentSessionPosition)
    }

    override fun onTabReselected() {
        binding.sessionsRecycler.smoothScrollToPosition(0)
    }

    override fun requestSavingScrollState() {
        val layoutManager = binding.sessionsRecycler.layoutManager as LinearLayoutManager
        PreviousSessionPrefs.scrollState = layoutManager.getScrollState()
    }

    override fun requestRestoringScrollState() {
        if (allSessionsViewModel.sessions is Result.Success<*>) {
            scrollToPreviousSession()
        } else {
            allSessionsViewModel.isNeedRestoreScrollState = true
        }
    }

    private fun scrollToPreviousSession() {
        allSessionsViewModel.isNeedRestoreScrollState = false
        val layoutManager = binding.sessionsRecycler.layoutManager as LinearLayoutManager
        layoutManager.restoreScrollState(PreviousSessionPrefs.scrollState)
        PreviousSessionPrefs.initPreviousSessionPrefs()
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

            addOnScrollListener(
                    onScrollStateChanged = { _: RecyclerView?, newState: Int ->
                        if (binding.sessionsRecycler.isGone()) return@addOnScrollListener
                        setDayHeaderVisibility(newState != RecyclerView.SCROLL_STATE_IDLE)
                    },
                    onScrolled = { _, _, _ ->
                        val linearLayoutManager = layoutManager as LinearLayoutManager
                        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
                        val dayNumber = sessionsSection.getDateNumberOrNull(firstPosition)
                        dayNumber ?: return@addOnScrollListener
                        val dayTitle = getString(R.string.session_day_title, dayNumber)
                        binding.dayHeader.setTextIfChanged(dayTitle)
                    })
            setLinearDivider(R.drawable.shape_divider_vertical_12dp,
                    layoutManager as LinearLayoutManager)
            recycledViewPool = sharedRecycledViewPool
            (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
    }

    private fun setDayHeaderVisibility(visibleDayHeader: Boolean) {
        val transition = TransitionInflater
                .from(context)
                .inflateTransition(R.transition.date_header_visibility)
        TransitionManager.beginDelayedTransition(binding.sessionsConstraintLayout, transition)
        binding.dayHeader.setVisible(visibleDayHeader)
    }

    companion object {
        fun newInstance(): AllSessionsFragment = AllSessionsFragment()
    }
}
