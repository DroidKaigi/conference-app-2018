package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class SearchSessionViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    val levelSessions: LiveData<Result<Map<Level, List<Session>>>> by lazy {
        repository.levelSessions
                .toResult(schedulerProvider)
                .toLiveData()
    }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onFavoriteClick(session: Session) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle.subscribeBy(onError = { e -> Timber.e(e) }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
