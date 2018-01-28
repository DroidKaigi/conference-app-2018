package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.model.Sponsor as ModelSponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup as ModelSponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan as ModelSponsorPlan
/*
fun SponsorPlanWithSponsorGroup.toSponsorPlanModel(): ModelSponsorPlan {
    val plan = sponsorPlan!!
    return ModelSponsorPlan(
            name = plan.name,
            type = plan.type.toSponsorType(),
            groups = listOf(ModelSponsorGroup(sponsors.map { it.toSponsor() }))

    )
}

private fun SponsorPlanEntity.Type.toSponsorType(): ModelSponsorPlan.Type
        = when (this) {
    SponsorPlanEntity.Type.PLATINUM -> ModelSponsorPlan.Type.Platinum
    SponsorPlanEntity.Type.GOLD -> ModelSponsorPlan.Type.Gold
    SponsorPlanEntity.Type.SILVER -> ModelSponsorPlan.Type.Silver
    SponsorPlanEntity.Type.SUPPORTER -> ModelSponsorPlan.Type.Supporter
    SponsorPlanEntity.Type.TECHNICAL_FOR_NETWORK -> ModelSponsorPlan.Type.TechnicalForNetwork
}

private fun SponsorEntity.toSponsor(): ModelSponsor {
    val (base64Img, imgUrl) = base64Img to imgUrl

    val imageUri = when {
        base64Img != null -> ModelSponsor.ImageUri.Base64ImageUri(base64Img)
        imgUrl != null -> ModelSponsor.ImageUri.NetworkImageUri(imgUrl)
        else -> throw IllegalStateException("a json file is broken.")
    }
    return ModelSponsor(link, imageUri)
}
*/
