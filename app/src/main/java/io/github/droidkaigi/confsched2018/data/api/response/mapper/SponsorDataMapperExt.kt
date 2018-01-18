package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup
import io.github.droidkaigi.confsched2018.data.api.response.SponsorGroup as ResponseSponsorGroup
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan as ResponseSponsorPlan
import io.github.droidkaigi.confsched2018.model.SponsorGroup as ModelSponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan as ModelSponsorPlan

fun ResponseSponsorPlan.toSponsorPlan(): ModelSponsorPlan =
        ModelSponsorPlan(
                planName,
                planType.toSponsorType(),
                groups.map { it.toSponsorGroup() }
        )

fun ResponseSponsorPlan.Type.toSponsorType(): ModelSponsorPlan.Type = when (this) {
    ResponseSponsorPlan.Type.PLATINUM -> ModelSponsorPlan.Type.Platinum
    ResponseSponsorPlan.Type.GOLD -> ModelSponsorPlan.Type.Gold
    ResponseSponsorPlan.Type.SILVER -> ModelSponsorPlan.Type.Silver
    ResponseSponsorPlan.Type.SUPPORTER -> ModelSponsorPlan.Type.Supporter
    ResponseSponsorPlan.Type.TECHNICAL_FOR_NETWORK -> ModelSponsorPlan.Type.TechnicalForNetwork
}

fun ResponseSponsorGroup.toSponsorGroup(): ModelSponsorGroup =
        SponsorGroup(sponsors.map {
            val (base64Img, imgUrl) = it.base64Img to it.imgUrl

            val imageUri = when {
                base64Img != null -> Sponsor.ImageUri.Base64ImageUri(base64Img)
                imgUrl != null -> Sponsor.ImageUri.NetworkImageUri(imgUrl)
                else -> throw IllegalStateException("a json file is broken.")
            }

            Sponsor(it.link, imageUri)
        })
