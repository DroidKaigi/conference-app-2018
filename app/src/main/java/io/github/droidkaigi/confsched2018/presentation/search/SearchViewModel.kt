package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    val sessions: MutableLiveData<Result<List<Session>>> = MutableLiveData()
    val speakers: MutableLiveData<Result<List<Speaker>>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onQuery(query: String) {
        repository.sessions
                .map {
                    it.filter { it.title.contains(query) || it.desc.contains(query) }
                }
                .toResult(schedulerProvider)
                .subscribe { sessions.value = it }
                .addTo(compositeDisposable)
        repository.speakers
                .map {
                    it.filter { it.name.contains(query) }
                }
                .toResult(schedulerProvider)
                .subscribe { speakers.value = it }
                .addTo(compositeDisposable)
    }

    fun onFavoriteClick(session: Session) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle.subscribe().addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
