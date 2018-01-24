package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsFeedbackBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SessionsFeedbackFragment : Fragment(), Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSessionsFeedbackBinding

    private val sessionsFeedbackViewModel: SessionsFeedbackViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SessionsFeedbackViewModel::class.java)
    }

    private val onSubmitListener = { sessionFeedback: SessionFeedback ->
        sessionsFeedbackViewModel.onSubmit(sessionFeedback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSessionsFeedbackBinding.inflate(
                inflater,
                container!!,
                false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionsFeedbackViewModel.sessionId = arguments!!.getString(EXTRA_SESSION_ID)
        sessionsFeedbackViewModel.sessionTitle = arguments!!.getString(EXTRA_SESSION_TITLE)

        sessionsFeedbackViewModel.mutableSessionFeedback.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    binding.sessionFeedback = result.data
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrBlank()) {
                    return
                }

                sessionsFeedbackViewModel.onSessionFeedbackChanged(sessionsFeedbackViewModel
                        .sessionFeedback.copy(totalEvaluation = Integer.parseInt(p0.toString())))
            }
        })

        binding.submit.setOnClickListener {
            onSubmitListener(sessionsFeedbackViewModel.sessionFeedback)
        }

        sessionsFeedbackViewModel.init()
    }

    override fun onDetach() {
        sessionsFeedbackViewModel.save()
        super.onDetach()
    }

    companion object {
        const val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        const val EXTRA_SESSION_TITLE = "EXTRA_SESSION_TITLE"
        fun newInstance(sessionId: String, sessionTitle: String): SessionsFeedbackFragment =
                SessionsFeedbackFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_SESSION_ID, sessionId)
                        putString(EXTRA_SESSION_TITLE, sessionTitle)
                    }
                }
    }
}
