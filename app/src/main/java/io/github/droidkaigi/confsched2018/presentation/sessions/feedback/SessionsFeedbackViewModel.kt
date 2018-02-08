package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Alert
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
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

    var isLoading = MutableLiveData<Boolean>()
    var alertMessage = MutableLiveData<Alert>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val sessionFlowable = repository.sessions
            .map { sessions ->
                sessions
                        .filterIsInstance<Session.SpeechSession>()
                        .first { it.id == sessionId }
            }.share()

    fun onSessionFeedbackChanged(sessionFeedback: SessionFeedback) {
        repository.saveSessionFeedback(sessionFeedback)
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    fun onSubmitClick(sessionFeedback: SessionFeedback) {
        if (sessionFeedback.fillouted) {
            alertMessage.value = Alert(Alert.Type.AlertDialog, R.string.session_feedback_confirm)
        } else {
            alertMessage.value = Alert(Alert.Type.Toast, R.string.session_feedback_not_fillout)
        }
    }

    fun submit(session: Session.SpeechSession, sessionFeedback: SessionFeedback) {
        repository.submitSessionFeedback(session, sessionFeedback)
                .doOnSubscribe { isLoading.postValue(true) }
                .doFinally {
                    isLoading.postValue(false)
                }
                .doOnComplete {
                    alertMessage.postValue(Alert(Alert.Type.Toast, R.string.submit_success))
                }
                .doOnError { alertMessage.postValue(Alert(Alert.Type.Toast, R.string
                        .submit_failure)) }
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
