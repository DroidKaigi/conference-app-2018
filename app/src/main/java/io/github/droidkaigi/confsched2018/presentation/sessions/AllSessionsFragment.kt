package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import io.github.droidkaigi.confsched2018.model.LoadState
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.common.view.OnTabReselectedListener
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.addOnScrollListener
import io.github.droidkaigi.confsched2018.util.ext.isGone
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.observeNonNull
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import io.github.droidkaigi.confsched2018.util.ext.setTextIfChanged
import io.github.droidkaigi.confsched2018.util.ext.setVisible
import timber.log.Timber
import javax.inject.Inject

class AllSessionsFragment
    : DaggerFragment(), OnTabReselectedListener {

    private lateinit var binding: FragmentAllSessionsBinding

    private val sessionsSection = DateSessionsSection()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sessionAlarm: SessionAlarm
    @Inject lateinit var sharedRecycledViewPool: RecyclerView.RecycledViewPool
    @Inject lateinit var allSessionsActionCreator: AllSessionsActionCreator

    @Inject lateinit var sessionsStore: SessionsStore
    private val allSessionsStore: AllSessionsStore by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AllSessionsStore::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        allSessionsActionCreator.favorite(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->

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
        sessionsStore.sessions.observeNonNull(this, { sessions ->
            sessionsSection.updateSessions(sessions, onFavoriteClickListener,
                    onFeedbackListener, true)

        })
        sessionsStore.loadingState.observe(this, { state ->
            progressTimeLatch.loading = state is LoadState.Loading
        })
        allSessionsStore.refreshLoadState.observe(this, { loadState ->
            val errorLoadState = loadState as? LoadState.Error ?: return@observe
            val e = errorLoadState.e

            // If user is offline, not error. So we write log to debug
            Timber.d(e)
            val view = view ?: return@observe
            Snackbar.make(view, R.string.session_fetch_failed, Snackbar.LENGTH_LONG).apply {
                setAction(R.string.session_load_retry) {
                    allSessionsActionCreator.refreshSessions()
                }
            }.show()
        })
        allSessionsActionCreator.refreshSessions()
    }

    override fun onTabReselected() {
        binding.sessionsRecycler.smoothScrollToPosition(0)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsSection)
            setOnItemClickListener({ item, _ ->
                val sessionItem = item as? SpeechSessionItem ?: return@setOnItemClickListener

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
