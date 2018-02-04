package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.map
import io.github.droidkaigi.confsched2018.util.ext.switchMap
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
    private val tabModeLiveData = MutableLiveData<SessionTabMode>().apply {
        value = SessionTabMode.ROOM
    }
    private val rooms: LiveData<Result<SessionTab>> by lazy {
        repository.rooms
                .map { SessionTab.Room(it) as SessionTab }
                .toResult(schedulerProvider)
                .toLiveData()
    }
    private val schedules: LiveData<Result<SessionTab>> by lazy {
        repository.schedules
                .map { SessionTab.Schedule(it) as SessionTab }
                .toResult(schedulerProvider)
                .toLiveData()
    }
    val tabMode: SessionTabMode
        get() = tabModeLiveData.value!!
    val tab: LiveData<Result<SessionTab>> = tabModeLiveData.switchMap {
        when (it) {
            SessionTabMode.ROOM -> rooms
            SessionTabMode.SCHEDULE -> schedules
        }
    }
    val isLoading: LiveData<Boolean> by lazy {
        tab.map { it.inProgress }
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

    fun changeTabMode(newTabMode: SessionTabMode) {
        tabModeLiveData.postValue(newTabMode)
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
