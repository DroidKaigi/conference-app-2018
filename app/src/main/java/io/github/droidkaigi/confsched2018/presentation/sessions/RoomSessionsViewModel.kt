package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RoomSessionsViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    val sessions: LiveData<Result<List<Session>>> by lazy {
        repository.roomSessions
                .map { t: Map<Room, List<Session>> ->
                    t.entries.first { it.key.name == roomName }.value
                }
                .toResult(schedulerProvider)
                .toLiveData()
    }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    lateinit var roomName: String

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
