package io.github.droidkaigi.confsched2018.presentation.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionDetailBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.ext.toVisible
import io.github.droidkaigi.confsched2018.util.lang
import timber.log.Timber
import javax.inject.Inject

class SessionDetailFragment : Fragment(), Injectable {
    // TODO create layout
    private lateinit var binding: FragmentSessionDetailBinding

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

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
                    bindSession(sessions.first { it.id == sessionId })
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        }
    }

    private fun bindSession(session: Session.SpeechSession) {
        binding.session = session
        binding.fab.setOnClickListener {
            sessionDetailViewModel.onFavoriteClick(session)
        }
        binding.sessionTopic.text = session.topic.getNameByLang(lang())
    }

    companion object {
        val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        fun newInstance(sessionId: String): SessionDetailFragment = SessionDetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SESSION_ID, sessionId)
            }
        }
    }
}
