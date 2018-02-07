package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorsEntities
import io.github.droidkaigi.confsched2018.data.db.SponsorDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSponsorPlan
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
            sponsorDatabase.getSponsors()
                    .filter { it.isNotEmpty() }
                    .map { it.toSponsorPlan() }

    @CheckResult override fun refreshSponsorPlans(ln: Lang): Completable =
            when (ln) {
                Lang.JA -> droidKaigiApi.sponsorPlansJa()
                Lang.EN -> droidKaigiApi.sponsorPlansEn()
            }.doOnSuccess {
                val plansResponse = it
                sponsorDatabase.save(plansResponse.toSponsorsEntities())
            }.subscribeOn(schedulerProvider.io())
                    .toCompletable()
}
