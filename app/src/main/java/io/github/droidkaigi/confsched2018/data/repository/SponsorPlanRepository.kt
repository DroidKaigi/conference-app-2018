package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Flowable

interface SponsorPlanRepository {
    val sponsorPlans: Flowable<List<SponsorPlan>>
}
