package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentTimeSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sessions.item.TimeBasedSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.ProgressTimeLatch
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import timber.log.Timber
import javax.inject.Inject

class TimeSessionsFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentTimeSessionsBinding

    private val sessionsSection = TimeBasedSessionsSection()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm

    private val timeSessionsViewModel: TimeSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TimeSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        timeSessionsViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTimeSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timeSessionsViewModel.schedule = arguments?.getSerializable(ARG_SCHEDULE) as? SessionSchedule ?: run {
            throw IllegalStateException("Start time must be given")
        }

        setupRecyclerView()

        val progressTimeLatch = ProgressTimeLatch {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
        }
        timeSessionsViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsSection.updateSessions(sessions, onFavoriteClickListener)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        timeSessionsViewModel.isLoading.observe(this, { isLoading ->
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
        }
    }

    companion object {
        private const val ARG_SCHEDULE = "schedule"

        fun newInstance(schedule: SessionSchedule): TimeSessionsFragment =
                TimeSessionsFragment().apply {
                    arguments = Bundle().apply {
                    putSerializable(ARG_SCHEDULE, schedule)
                }
            }
    }
}
