package io.github.droidkaigi.confsched2018.presentation.favorite

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class FavoriteSessionsViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val shownSessionIds = mutableSetOf<String>()

    val sessions: LiveData<Result<List<Session.SpeechSession>>> by lazy {
        repository.sessions
                .map {
                    it
                            .filterIsInstance<Session.SpeechSession>()
                            .filter { it.isFavorited || shownSessionIds.contains(it.id) }
                }
                .doOnNext {
                    shownSessionIds.addAll(it.map { it.id })
                }
                .toResult(schedulerProvider)
                .toLiveData()
    }

    fun onFavoriteClick(session: Session.SpeechSession) {
        val favoriteSingle: Single<Boolean> = repository.favorite(session)
        favoriteSingle
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        shownSessionIds.clear()
    }
}
