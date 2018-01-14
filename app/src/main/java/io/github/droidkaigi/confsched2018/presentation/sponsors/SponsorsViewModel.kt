package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.SponsorPlanRepository
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class SponsorsViewModel @Inject constructor(
        private val sponsorPlanRepository: SponsorPlanRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val sponsors: LiveData<Result<List<SponsorPlan>>> by lazy {
        sponsorPlanRepository.sponsorPlans()
                .doOnSubscribe {
                    compositeDisposable.add(Disposables.fromSubscription(it))
                }
                .toResult(schedulerProvider)
                .toLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

