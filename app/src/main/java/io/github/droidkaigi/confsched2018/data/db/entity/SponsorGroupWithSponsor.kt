package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SponsorGroupWithSponsor(
        @Embedded
        var sponsorPlan: SponsorGroupEntity? = null,
        @Relation(parentColumn = "id", entityColumn = "group_id", entity = SponsorEntity::class)
        var sponsors: List<SponsorEntity> = emptyList()
)
