package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.repository.SponsorPlanRepository
import io.github.droidkaigi.confsched2018.model.SponsorPlan
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

class SponsorsViewModel @Inject constructor(
        private val sponsorPlanRepository: SponsorPlanRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()

    val sponsors: LiveData<Result<List<SponsorPlan>>> by lazy {
        sponsorPlanRepository.sponsorPlans()
                .toResult(schedulerProvider)
                .toLiveData()
    }

    val isLoading: LiveData<Boolean> by lazy {
        sponsors.map { it.inProgress }
    }

    @VisibleForTesting
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        sponsorPlanRepository
                .refreshSponsorPlans()
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}

