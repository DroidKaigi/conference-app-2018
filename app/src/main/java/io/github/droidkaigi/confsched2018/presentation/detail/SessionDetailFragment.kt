package io.github.droidkaigi.confsched2018.presentation.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.ItemSessionBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SessionDetailFragment : Fragment(), Injectable {
    // TODO create layout
    private lateinit var binding: ItemSessionBinding

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsViewModel: SessionDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SessionDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ItemSessionBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionsViewModel.sessionId = arguments!!.getString(EXTRA_SESSION_ID)
        lifecycle.addObserver(sessionsViewModel)

        sessionsViewModel.session.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val session = result.data
                    binding.session = session
                    binding.favorite.setOnClickListener {
                        sessionsViewModel.onFavoriteClick(session)
                    }
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        }
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
