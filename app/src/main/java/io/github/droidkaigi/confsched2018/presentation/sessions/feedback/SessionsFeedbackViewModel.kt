package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SessionsFeedbackViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    lateinit var sessionId: String

    val sessionFeedback: LiveData<Result<SessionFeedback>> by lazy {
        sessionFlowable
                .map { session ->
                    session.feedback
                }
                .toResult(schedulerProvider)
                .toLiveData()
    }

    val session: LiveData<Result<Session.SpeechSession>> by lazy {
        sessionFlowable.toResult(schedulerProvider)
                .toLiveData()
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val sessionFlowable = repository.sessions
            .map { sessions ->
                sessions
                        .filterIsInstance<Session.SpeechSession>()
                        .first { it.id == sessionId }
            }.share()

    init {


    }

    fun onSessionFeedbackChanged(sessionFeedback: SessionFeedback) {
        repository.saveSessionFeedback(sessionFeedback)
                .subscribe()
                .addTo(compositeDisposable)
    }

    fun onSubmit(sessionFeedback: SessionFeedback) {
        (session.value as? Result.Success)?.data?.also {
            // TODO: if submit success, add to save local DB change sessionFeedback.submitted = ture
            repository.submitSessionFeedback(it, sessionFeedback)
                    .subscribe()
                    .addTo(compositeDisposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
