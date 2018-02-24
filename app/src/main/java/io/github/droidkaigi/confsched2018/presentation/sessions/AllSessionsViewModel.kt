package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.map
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AllSessionsViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mutableRefreshState: MutableLiveData<Result<Unit>> = MutableLiveData()
    val refreshResult: LiveData<Result<Unit>> = mutableRefreshState

    val sessions: LiveData<Result<List<Session>>> by lazy {
        repository.sessions
                .toResult(schedulerProvider)
                .toLiveData()
    }

    val isLoading: LiveData<Boolean> by lazy {
        sessions.map { it.inProgress }
    }

    fun onFavoriteClick(session: Session.SpeechSession) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refreshSessions()
    }

    private fun refreshSessions() {
        repository
                .refreshSessions()
                .toResult<Unit>(schedulerProvider)
                .subscribeBy(
                        onNext = { mutableRefreshState.postValue(it) },
                        onError = defaultErrorHandler()
                )
                .addTo(compositeDisposable)
    }

    fun onRetrySessions() {
        refreshSessions()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
