package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SponsorPlanWithSponsor(
        @Embedded
        var sponsorGroup: SponsorPlanEntity? = null,
        @Relation(parentColumn = "id", entityColumn = "plan_id")
        var sponsors: List<SponsorEntity> = emptyList()
)
