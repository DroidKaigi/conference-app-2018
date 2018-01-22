package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity


fun List<SponsorPlan>.toSponsorEntities(): List<SponsorEntity> =
    mapIndexed { index, plan ->
        plan.groups.flatMap { it.sponsors }
                .map { sponsor ->
                    SponsorEntity(
                            planId = index,
                            link = sponsor.link,
                            base64Img = sponsor.base64Img,
                            imgUrl = sponsor.imgUrl
                    )
                }
    }.flatMap { it }


fun List<SponsorPlan>.toSponsorPlanEntities(): List<SponsorPlanEntity> =
        mapIndexed { index, plan ->
            SponsorPlanEntity(
                    id = index,
                    name = plan.planName,
                    type = SponsorPlanEntity.Type.valueOf(plan.planType.name))
        }

