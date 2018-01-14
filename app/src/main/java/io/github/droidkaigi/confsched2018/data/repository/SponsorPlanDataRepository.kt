package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.SponsorApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorPlan
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Flowable
import javax.inject.Inject
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type as ResponseSponsorType

class SponsorPlanDataRepository @Inject constructor(
        private val sponsorApi: SponsorApi
) : SponsorPlanRepository {

    override fun sponsorPlans(ln: Lang): Flowable<List<SponsorPlan>> =
            when (ln) {
                Lang.JA -> sponsorApi.sponsorPlansJp()
                Lang.EN -> sponsorApi.sponsorPlansEn()
            }.map {
                it.map { it.toSponsorPlan() }
            }.toFlowable()
}
