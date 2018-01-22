package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SponsorPlanWithSponsor(
    @Embedded
    var sponsorPlan: SponsorPlanEntity? = null,
    @Relation(parentColumn = "id", entityColumn = "userId")
    var sponsors: List<SponsorEntity> = emptyList()
)
