package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorGroupWithSponsor
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import io.github.droidkaigi.confsched2018.model.Sponsor as ModelSponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup as ModelSponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan as ModelSponsorPlan

fun SponsorPlanEntity.toSponsorPlanModel(groups: List<ModelSponsorGroup>): ModelSponsorPlan {
    return ModelSponsorPlan(
            name = name,
            type = type.toSponsorType(),
            groups = groups
    )
}

fun List<SponsorGroupWithSponsor>.toSponsorGroupModels():List<ModelSponsorGroup> =
        map {  ModelSponsorGroup(it.sponsors.toSponsorModels())}

private fun List<SponsorEntity>.toSponsorModels(): List<ModelSponsor> =
        map { it.toSponsorModel() }


private fun SponsorPlanEntity.Type.toSponsorType(): ModelSponsorPlan.Type = when (this) {
    SponsorPlanEntity.Type.PLATINUM -> ModelSponsorPlan.Type.Platinum
    SponsorPlanEntity.Type.GOLD -> ModelSponsorPlan.Type.Gold
    SponsorPlanEntity.Type.SILVER -> ModelSponsorPlan.Type.Silver
    SponsorPlanEntity.Type.SUPPORTER -> ModelSponsorPlan.Type.Supporter
    SponsorPlanEntity.Type.TECHNICAL_FOR_NETWORK -> ModelSponsorPlan.Type.TechnicalForNetwork
}

private fun SponsorEntity.toSponsorModel(): ModelSponsor {
    val (base64Img, imgUrl) = base64Img to imgUrl

    val imageUri = when {
        base64Img != null -> ModelSponsor.ImageUri.Base64ImageUri(base64Img)
        imgUrl != null -> ModelSponsor.ImageUri.NetworkImageUri(imgUrl)
        else -> throw IllegalStateException("a json file is broken.")
    }
    return ModelSponsor(link, imageUri)
}
