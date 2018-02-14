package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity

fun List<SponsorPlan>.toSponsorsEntities(): List<SponsorEntity> =
        map { plan ->
            plan.groups.mapIndexed { index, group ->
                group.sponsors.map { sponsor ->
                    SponsorEntity(
                            groupIndex = index,
                            plan = plan.toSponsorEntity(),
                            link = sponsor.link,
                            base64Img = sponsor.base64Img,
                            imgUrl = sponsor.imgUrl)
                }
            }.flatten()
        }.flatten()

fun SponsorPlan.toSponsorEntity(): SponsorPlanEntity {
    return SponsorPlanEntity(name = planName,
            type = planType.toSponsorType())
}

private fun SponsorPlan.Type.toSponsorType(): SponsorPlanEntity.Type = when (this) {
    SponsorPlan.Type.PLATINUM -> SponsorPlanEntity.Type.PLATINUM
    SponsorPlan.Type.GOLD -> SponsorPlanEntity.Type.GOLD
    SponsorPlan.Type.SILVER -> SponsorPlanEntity.Type.SILVER
    SponsorPlan.Type.SUPPORTER -> SponsorPlanEntity.Type.SUPPORTER
    SponsorPlan.Type.TECHNICAL_FOR_NETWORK -> SponsorPlanEntity.Type.TECHNICAL_FOR_NETWORK
}
