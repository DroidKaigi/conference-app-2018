package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSessionsFeedbackBinding
import io.github.droidkaigi.confsched2018.model.Alert
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.view.FeedbackRankingView
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.observeNonNull
import io.github.droidkaigi.confsched2018.util.ext.setVisible
import timber.log.Timber
import javax.inject.Inject

class SessionsFeedbackFragment : DaggerFragment() {

    private lateinit var binding: FragmentSessionsFeedbackBinding

    private val sessionsFeedbackViewModel: SessionsFeedbackViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)
                .get(SessionsFeedbackViewModel::class.java)
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val onCurrentRankingChangeListener = object
        : FeedbackRankingView.OnCurrentRankingChangeListener {
        override fun onCurrentRankingChange(view: FeedbackRankingView, currentRanking: Int) {

            val old = (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)
                    ?.data ?: return
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

    private val onPositiveButtonListener = DialogInterface.OnClickListener { _, _ ->
        val session = (sessionsFeedbackViewModel.session.value as? Result.Success)
                ?.data ?: return@OnClickListener
        val sessionFeedback = (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)
                ?.data ?: return@OnClickListener
        sessionsFeedbackViewModel.submit(session, sessionFeedback.copy(submitted = true))
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

        setUpFeedbackView()
        setUpAlertView()

        sessionsFeedbackViewModel.isLoading.observeNonNull(this, {
            binding.progress.setVisible(it)
        })
    }

    private fun setUpFeedbackView() {
        sessionsFeedbackViewModel.sessionFeedback.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessionFeedback = result.data
                    binding.sessionFeedback = sessionFeedback
                    binding.submit.text =
                            if (sessionFeedback.submitted) {
                                getString(R.string.submitted)
                            } else {
                                getString(R.string.submit_feedback)
                            }
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
            val sessionFeedback =
                    (sessionsFeedbackViewModel.sessionFeedback.value as? Result.Success)
                            ?.data ?: return@setOnClickListener
            sessionsFeedbackViewModel.onSubmitClick(sessionFeedback)
        }
    }

    private fun setUpAlertView() {
        sessionsFeedbackViewModel.alertMessage.observeNonNull(this, {
            when (it.type) {
                Alert.Type.Toast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                Alert.Type.AlertDialog -> {
                    AlertDialog.Builder(context!!)
                            .setTitle(it.message)
                            .setPositiveButton(android.R.string.ok, onPositiveButtonListener)
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                }
            }
        })
    }

    companion object {
        fun newInstance(): SessionsFeedbackFragment =
                SessionsFeedbackFragment()
    }
}
