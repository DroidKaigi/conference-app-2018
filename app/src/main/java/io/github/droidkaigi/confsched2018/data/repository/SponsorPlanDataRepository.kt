package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.SponsorApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorPlan
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class SponsorPlanDataRepository @Inject constructor(
        private val sponsorApi: SponsorApi
) : SponsorPlanRepository {
    private val processor: FlowableProcessor<List<SponsorPlan>> = PublishProcessor.create()

    override fun sponsorPlans(): Flowable<List<SponsorPlan>> = processor.toSerialized()

    override fun refreshSponsorPlans(ln: Lang): Completable =
            when (ln) {
                Lang.JA -> sponsorApi.sponsorPlansJp()
                Lang.EN -> sponsorApi.sponsorPlansEn()
            }.map {
                it.map { it.toSponsorPlan() }
            }.doOnSuccess {
                processor.onNext(it)
            }.doOnError {
                processor.onError(it)
            }.toCompletable()
}
