package io.github.droidkaigi.confsched2018.presentation.speaker

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSpeakerDetailBinding
import io.github.droidkaigi.confsched2018.di.Injectable
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

class SpeakerDetailFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSpeakerDetailBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsSection = SimpleSessionsSection()
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm

    private val speakerDetailViewModel: SpeakerDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SpeakerDetailViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        speakerDetailViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =
                FragmentSpeakerDetailBinding.inflate(
                        inflater,
                        container!!,
                        false
                )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        speakerDetailViewModel.speakerId = arguments!!.getString(EXTRA_SPEAKER_ID)
        speakerDetailViewModel.speakerSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val speaker = result.data.first
                    binding.speaker = speaker
                    sessionsSection.updateSessions(result.data.second,
                            onFavoriteClickListener,
                            userIdInDetail = speaker.id)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
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
        val linearLayoutManager = LinearLayoutManager(context)
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            setLinearDivider(R.drawable.shape_divider_vertical_6dp, linearLayoutManager)
        }
    }

    companion object {
        const val EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID"
        fun newInstance(speakerId: String): SpeakerDetailFragment = SpeakerDetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SPEAKER_ID, speakerId)
            }
        }
    }
}
