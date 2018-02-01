package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan

fun List<SponsorEntity>.toSponsorPlan(): List<SponsorPlan> {
    return groupBy { it.plan }
            .map { entry: Map.Entry<SponsorPlanEntity, List<SponsorEntity>> ->
                val sponsorGroup: List<SponsorGroup> = entry.value.groupBy { it.groupIndex }.map {
                    SponsorGroup(it.value.map { it.toSponsor() })
                }
                entry.key.toSponsorPlan(sponsorGroup)
            }
}

private fun SponsorPlanEntity.Type.toSponsorType(): SponsorPlan.Type = when (this) {
    SponsorPlanEntity.Type.PLATINUM -> SponsorPlan.Type.Platinum
    SponsorPlanEntity.Type.GOLD -> SponsorPlan.Type.Gold
    SponsorPlanEntity.Type.SILVER -> SponsorPlan.Type.Silver
    SponsorPlanEntity.Type.SUPPORTER -> SponsorPlan.Type.Supporter
    SponsorPlanEntity.Type.TECHNICAL_FOR_NETWORK -> SponsorPlan.Type.TechnicalForNetwork
}

private fun SponsorPlanEntity.toSponsorPlan(groups: List<SponsorGroup>):
        SponsorPlan {
    return SponsorPlan(
            name = name,
            type = type.toSponsorType(),
            groups = groups
    )
}

private fun SponsorEntity.toSponsor(): Sponsor {
    val (base64Img, imgUrl) = base64Img to imgUrl

    val imageUri = when {
        base64Img != null -> Sponsor.ImageUri.Base64ImageUri(base64Img)
        imgUrl != null -> Sponsor.ImageUri.NetworkImageUri(imgUrl)
        else -> throw IllegalStateException("a json file is broken.")
    }
    return Sponsor(link, imageUri)
}
