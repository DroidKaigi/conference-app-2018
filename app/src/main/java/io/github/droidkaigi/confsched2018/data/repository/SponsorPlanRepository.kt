package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.util.lang
import io.reactivex.Completable
import io.reactivex.Flowable

interface SponsorPlanRepository {
    @CheckResult fun sponsorPlans(): Flowable<List<SponsorPlan>>

    @CheckResult fun refreshSponsorPlans(ln: Lang = lang()): Completable
}
