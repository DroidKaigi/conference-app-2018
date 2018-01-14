package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.map
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SessionsViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    val rooms: LiveData<Result<List<Room>>> by lazy {
        repository.rooms
                .toResult(schedulerProvider)
                .toLiveData()
    }
    val isLoading: LiveData<Boolean> by lazy {
        rooms.map { it.inProgress }
    }
    private val mutableRefreshState: MutableLiveData<Result<Unit>> = MutableLiveData()
    val refreshResult: LiveData<Result<Unit>> = mutableRefreshState

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refreshSessions()
    }

    fun onRetrySessions() {
        refreshSessions()
    }

    private fun refreshSessions() {
        repository
                .refreshSessions()
                .toResult<Unit>(schedulerProvider)
                .subscribeBy(
                        onNext = { mutableRefreshState.value = it },
                        onError = defaultErrorHandler()
                )
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
