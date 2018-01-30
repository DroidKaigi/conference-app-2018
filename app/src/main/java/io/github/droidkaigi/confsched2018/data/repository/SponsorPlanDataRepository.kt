package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.SponsorDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSponsorPlanModels
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class SponsorPlanDataRepository @Inject constructor(
        private val droidKaigiApi: DroidKaigiApi,
        private val sponsorDatabase: SponsorDatabase,
        private val schedulerProvider: SchedulerProvider
) : SponsorPlanRepository {

    override fun sponsorPlans(): Flowable<List<SponsorPlan>> =
            sponsorDatabase.getAllSponsorPlan()
                    .filter { it.isNotEmpty() }
                    .map { it.toSponsorPlanModels() }

    @CheckResult override fun refreshSponsorPlans(ln: Lang): Completable =
            when (ln) {
                Lang.JA -> droidKaigiApi.sponsorPlansJa()
                Lang.EN -> droidKaigiApi.sponsorPlansEn()
            }.doOnSuccess {
                sponsorDatabase.save(it)
                sponsorDatabase.getAllSponsorPlan()
                        .subscribe { }
            }.subscribeOn(schedulerProvider.computation())
                    .toCompletable()
}
