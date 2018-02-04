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
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsFeedbackBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.view.FeedbackRankingView
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SessionsFeedbackFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentSessionsFeedbackBinding

    private val sessionsFeedbackViewModel: SessionsFeedbackViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
                .get(SessionsFeedbackViewModel::class.java)
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val onCurrentRankingChangeListener: FeedbackRankingView.OnCurrentRankingChangeListener
            = object : FeedbackRankingView.OnCurrentRankingChangeListener {
        override fun onCurrentRankingChange(view: FeedbackRankingView, currentRanking: Int) {
            val old = (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)?.data!!
            val new = when (view.id) {
                R.id.total_evaluation -> old.copy(totalEvaluation = currentRanking)
                R.id.relevancy -> old.copy(relevancy = currentRanking)
                R.id.as_expected -> old.copy(asExpected = currentRanking)
                R.id.difficulty -> old.copy(difficulty = currentRanking)
                R.id.knowledgeable -> old.copy(knowledgeable = currentRanking)
                else -> old
            }
            sessionsFeedbackViewModel.onSessionFeedbackChanged(new)
        }
    }

    private val onCommentChangedListener: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            val old = (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)?.data!!
            val new = old.copy(comment = p0.toString())
            sessionsFeedbackViewModel.onSessionFeedbackChanged(new)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    private val onSubmitListener = { sessionFeedback: SessionFeedback ->
        sessionsFeedbackViewModel.onSubmit(sessionFeedback.copy(submitted = true))
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

        sessionsFeedbackViewModel.sessionFeedback.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    binding.sessionFeedback = result.data
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })

        binding.totalEvaluation.setListener(onCurrentRankingChangeListener)
        binding.relevancy.setListener(onCurrentRankingChangeListener)
        binding.asExpected.setListener(onCurrentRankingChangeListener)
        binding.difficulty.setListener(onCurrentRankingChangeListener)
        binding.knowledgeable.setListener(onCurrentRankingChangeListener)
        binding.comment.addTextChangedListener(onCommentChangedListener)

        binding.submit.setOnClickListener {
            onSubmitListener(
                    (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)?.data!!)
        }
    }

    companion object {
        fun newInstance(): SessionsFeedbackFragment =
                SessionsFeedbackFragment()
    }
}
