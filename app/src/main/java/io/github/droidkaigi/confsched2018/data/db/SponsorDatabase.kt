package io.github.droidkaigi.confsched2018.data.db

import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanWithSponsor
import io.reactivex.Flowable

interface SponsorDatabase {

    fun getAllSponsorPlan(): Flowable<List<SponsorPlanWithSponsor>>

    fun save(plans: List<SponsorPlan>)
}
