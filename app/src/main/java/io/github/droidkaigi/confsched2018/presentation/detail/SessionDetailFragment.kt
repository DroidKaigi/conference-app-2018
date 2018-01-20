package io.github.droidkaigi.confsched2018.presentation.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionDetailBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.lang
import timber.log.Timber
import javax.inject.Inject

class SessionDetailFragment : Fragment(), Injectable {
    // TODO create layout
    private lateinit var binding: FragmentSessionDetailBinding

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sessionAlarm: SessionAlarm

    private val sessionDetailViewModel: SessionDetailViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(SessionDetailViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSessionDetailBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionId = arguments!!.getString(EXTRA_SESSION_ID)
        sessionDetailViewModel.sessions.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    val position = sessions.indexOfFirst { it.id == sessionId }
                    bindSession(sessions[position])
                    setSessionIndicator(sessions.getOrNull(position - 1),
                            sessions.getOrNull(position + 1))
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        }

        binding.detailSessionsPrevSession.setOnClickListener {
            (activity as? OnClickBottomAreaListener)?.onClickPrevSession()
        }
        binding.detailSessionsNextSession.setOnClickListener {
            (activity as? OnClickBottomAreaListener)?.onClickNextSession()
        }

        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            binding.toolbarTextColorFactor = factor
        }

        binding.toolbar.setNavigationOnClickListener { activity?.finish() }
    }

    private fun bindSession(session: Session.SpeechSession) {
        binding.session = session
        binding.fab.setOnClickListener {
            sessionDetailViewModel.onFavoriteClick(session)
            sessionAlarm.toggleRegister(session)
        }
        binding.sessionTopic.text = session.topic.getNameByLang(lang())
    }

    private fun setSessionIndicator(prevSession: Session.SpeechSession?,
                                    nextSession: Session.SpeechSession?) {
        binding.prevSession = prevSession
        binding.nextSession = nextSession
    }

    interface OnClickBottomAreaListener {
        fun onClickPrevSession()
        fun onClickNextSession()
    }

    companion object {
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        fun newInstance(sessionId: String): SessionDetailFragment = SessionDetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SESSION_ID, sessionId)
            }
        }
    }
}
