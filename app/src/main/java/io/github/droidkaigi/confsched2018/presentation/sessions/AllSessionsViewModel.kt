package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.mapper.toResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AllSessionsViewModel @Inject constructor(val repository: SessionRepository) : ViewModel() {
    val sessions: MutableLiveData<Result<List<Session>>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        repository.sessions
                .toResult()
                .subscribeBy { sessions.value = it }
                .addTo(compositeDisposable)
    }

    fun onCreated() {
        repository
                .refreshSessions()
                .subscribeBy(onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}