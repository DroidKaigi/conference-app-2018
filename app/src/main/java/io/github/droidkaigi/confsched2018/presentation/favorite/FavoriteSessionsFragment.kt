package io.github.droidkaigi.confsched2018.presentation.favorite

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
import io.github.droidkaigi.confsched2018.databinding.FragmentFavoriteSessionsBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sessions.item.FavoriteSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.addOnScrollListener
import io.github.droidkaigi.confsched2018.util.ext.isGone
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import io.github.droidkaigi.confsched2018.util.ext.setTextIfChanged
import io.github.droidkaigi.confsched2018.util.ext.setVisible
import timber.log.Timber
import javax.inject.Inject

class FavoriteSessionsFragment : DaggerFragment() {

    private lateinit var binding: FragmentFavoriteSessionsBinding

    private val sessionsSection = FavoriteSessionsSection()

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sharedRecycledViewPool: RecyclerView.RecycledViewPool
    private val sessionsViewModel: FavoriteSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FavoriteSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        sessionsViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        progressTimeLatch.loading = true
        sessionsViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    progressTimeLatch.loading = false
                    val sessions = result.data
                    sessionsSection.updateSessions(
                            sessions, onFavoriteClickListener, onFeedbackListener)
                    binding.mysessionInactiveGroup.setVisible(sessions.isEmpty())
                    binding.sessionsRecycler.setVisible(sessions.isNotEmpty())
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                    progressTimeLatch.loading = false
                }
            }
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
        fun newInstance(): FavoriteSessionsFragment = FavoriteSessionsFragment()
    }
}
