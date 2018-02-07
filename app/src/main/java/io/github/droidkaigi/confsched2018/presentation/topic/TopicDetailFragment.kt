package io.github.droidkaigi.confsched2018.presentation.topic

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.os.bundleOf
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentTopicDetailBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SimpleSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import timber.log.Timber
import javax.inject.Inject

class TopicDetailFragment : DaggerFragment() {

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentTopicDetailBinding
    private val sessionsSection = SimpleSessionsSection()

    private val topicDetailViewModel: TopicDetailViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(TopicDetailViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        topicDetailViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTopicDetailBinding.inflate(
                inflater,
                container!!,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        topicDetailViewModel.topicId = arguments!!.getInt(EXTRA_TOPIC_ID)
        topicDetailViewModel.topicSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    sessionsSection.updateSessions(result.data.second,
                            onFavoriteClickListener,
                            onFeedbackListener)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(topicDetailViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsSection)
            setOnItemClickListener { item, _ ->
                val sessionItem = item as? SpeechSessionItem ?: return@setOnItemClickListener
                navigationController.navigateToSessionDetailActivity(sessionItem.session)
            }
        }
        val linearLayoutManager = LinearLayoutManager(context)
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            setLinearDivider(R.drawable.shape_divider_vertical_12dp, linearLayoutManager)
        }
    }

    companion object {
        const val EXTRA_TOPIC_ID = "EXTRA_TOPIC_ID"
        fun newInstance(topicId: Int): TopicDetailFragment = TopicDetailFragment().apply {
            arguments = bundleOf(EXTRA_TOPIC_ID to topicId)
        }
    }
}
