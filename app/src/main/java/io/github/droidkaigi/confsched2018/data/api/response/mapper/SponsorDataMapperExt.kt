package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.SponsorGroup
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity


fun List<SponsorGroup>.toSponsorGroupEntities(planId: Int): List<SponsorEntity> =
        mapIndexed { index, group ->
            group.sponsors.map { sponsor ->
                SponsorEntity(
                        groupIndex = index,
                        planId = planId,
                        link = sponsor.link,
                        base64Img = sponsor.base64Img,
                        imgUrl = sponsor.imgUrl)
            }
        }.flatten()

fun List<SponsorPlan>.toSponsorPlanEntities(): List<SponsorPlanEntity> =
        map { plan ->
            SponsorPlanEntity(name = plan.planName,
                    type = plan.planType.toSponsorType())
        }

private fun SponsorPlan.Type.toSponsorType(): SponsorPlanEntity.Type = when (this) {
    SponsorPlan.Type.PLATINUM -> SponsorPlanEntity.Type.PLATINUM
    SponsorPlan.Type.GOLD -> SponsorPlanEntity.Type.GOLD
    SponsorPlan.Type.SILVER -> SponsorPlanEntity.Type.SILVER
    SponsorPlan.Type.SUPPORTER -> SponsorPlanEntity.Type.SUPPORTER
    SponsorPlan.Type.TECHNICAL_FOR_NETWORK -> SponsorPlanEntity.Type.TECHNICAL_FOR_NETWORK
}
