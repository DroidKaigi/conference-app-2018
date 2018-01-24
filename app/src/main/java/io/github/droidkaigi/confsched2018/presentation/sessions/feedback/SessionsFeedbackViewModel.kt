package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SessionsFeedbackViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    var sessionId: String = ""
    var sessionTitle: String = ""

    lateinit var sessionFeedback: SessionFeedback
    var mutableSessionFeedback = MutableLiveData<Result<SessionFeedback>>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun init() {
        repository.sessionFeedbacks
                .map { sessionFeedbacks ->
                    sessionFeedback = sessionFeedbacks.firstOrNull { it.sessionId == sessionId }
                            ?: SessionFeedback(sessionId, sessionTitle, 0, 0, 0, 0, 0, "", false)
                    sessionFeedback
                }
                .toResult(schedulerProvider)
                .subscribe {
                    mutableSessionFeedback.value = it
                }
                .addTo(compositeDisposable)

    }

    fun onSessionFeedbackChanged(sessionFeedback: SessionFeedback) {
        this.sessionFeedback = sessionFeedback
        Observable.just(this.sessionFeedback)
                .toResult(schedulerProvider)
                .subscribe {
                    mutableSessionFeedback.value = it
                }
                .addTo(compositeDisposable)
    }

    fun save() {
        repository.saveSessionFeedback(sessionFeedback)
                .subscribe()
                .addTo(compositeDisposable)
    }

    fun onSubmit(sessionFeedback: SessionFeedback) {
        // TODO: if submit success, add to save local DB change sessionFeedback.submitted = ture
        repository.submitSessionFeedback(sessionFeedback)
                .subscribe()
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
