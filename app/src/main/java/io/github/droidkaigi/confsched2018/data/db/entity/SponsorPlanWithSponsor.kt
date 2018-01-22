package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded

data class SponsorPlanWithSponsor(
    @Embedded
    var sponsorPlan: SponsorPlanEntity? = null
)
