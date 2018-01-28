package io.github.droidkaigi.confsched2018.data.db

import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorGroupWithSponsor
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import io.reactivex.Flowable

interface SponsorDatabase {

    fun getAllSponsorPlan(): Flowable<List<Pair<SponsorPlanEntity, List<SponsorGroupWithSponsor>>>>

    fun save(plans: List<SponsorPlan>)
}
