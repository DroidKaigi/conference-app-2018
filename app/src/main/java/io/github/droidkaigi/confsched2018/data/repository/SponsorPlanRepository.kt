package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.util.lang
import io.reactivex.Flowable

interface SponsorPlanRepository {
    fun sponsorPlans(ln: Lang = lang()): Flowable<List<SponsorPlan>>
}
