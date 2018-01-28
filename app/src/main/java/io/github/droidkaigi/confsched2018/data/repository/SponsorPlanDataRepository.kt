package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.db.SponsorDatabase
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class SponsorPlanDataRepository @Inject constructor(
        private val droidKaigiApi: DroidKaigiApi,
        private val sponsorDatabase: SponsorDatabase
) : SponsorPlanRepository {
    private val processor: FlowableProcessor<List<SponsorPlan>> = PublishProcessor.create()

    override fun sponsorPlans(): Flowable<List<SponsorPlan>> = processor.toSerialized()

    override fun refreshSponsorPlans(ln: Lang): Completable =
            when (ln) {
                Lang.JA -> droidKaigiApi.sponsorPlansJa()
                Lang.EN -> droidKaigiApi.sponsorPlansEn()
            }.doOnSuccess {
                 sponsorDatabase.save(it)
            }.map {
                //it.map { it.toSponsorPlan() }
            }.doOnSuccess {
                //processor.onNext(it)
            }.doOnError {
                processor.onError(it)
            }.toCompletable()
}
