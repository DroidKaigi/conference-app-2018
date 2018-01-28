package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Sponsor
import io.github.droidkaigi.confsched2018.data.api.response.SponsorGroup
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorGroupEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity


fun List<SponsorGroup>.toSponsorGroupEntities(planId: Int): List<SponsorGroupEntity> =
        map { SponsorGroupEntity(planId = planId) }


fun List<Sponsor>.toSponsorEntities(group: Int): List<SponsorEntity> =
        map { sponsor ->
            SponsorEntity(groupId = group,
                    link = sponsor.link,
                    base64Img = sponsor.base64Img,
                    imgUrl = sponsor.imgUrl)
        }

fun List<SponsorPlan>.toSponsorPlanEntities(): List<SponsorPlanEntity> =
        map { plan ->
            SponsorPlanEntity(name = plan.planName,
                    type = SponsorPlanEntity.Type.valueOf(plan.planType.name))
        }

