package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val mutableResult = MutableLiveData<Result<SearchResult>>()
    val result: LiveData<Result<SearchResult>> = mutableResult

    var searchQuery: String = ""
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onQuery(query: String) {
        repository.search(query)
                .toResult(schedulerProvider)
                .subscribe {
                    mutableResult.value = it
                    searchQuery = query
                }
                .addTo(compositeDisposable)
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
    }
}
