package io.github.droidkaigi.confsched2018.presentation.contributor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.ContributorRepository
import io.github.droidkaigi.confsched2018.model.Contributor
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.defaultErrorHandler
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ContributorsViewModel @Inject constructor(
        private val repository: ContributorRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val contributors: LiveData<Result<List<Contributor>>> by lazy {
        repository.contributors
                .toResult(schedulerProvider)
                .toLiveData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refreshContributors()
    }

    fun onRefreshContributors() {
        refreshContributors()
    }

    private fun refreshContributors() {
        repository.loadContributors()
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
