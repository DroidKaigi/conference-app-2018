package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.SponsorApi
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Flowable
import javax.inject.Inject
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type as ResponseSponsorType

class SponsorPlanDataRepository @Inject constructor(
        private val sponsorApi: SponsorApi
) : SponsorPlanRepository {
    override val sponsorPlans: Flowable<List<SponsorPlan>>
        get() = sponsorApi.sponsorPlans().map {
            it.map {
                SponsorPlan(it.planName, when (it.planType) {
                    ResponseSponsorType.PLATINUM -> SponsorPlan.Type.Platinum
                    ResponseSponsorType.GOLD -> SponsorPlan.Type.Gold
                    ResponseSponsorType.SILVER -> SponsorPlan.Type.Silver
                    ResponseSponsorType.SUPPORTER -> SponsorPlan.Type.Supporter
                    ResponseSponsorType.TECHNICAL_FOR_NETWORK -> SponsorPlan.Type.TechnicalForNetwork
                }, it.groups.map {
                    SponsorGroup(it.sponsors.map {
                        val (base64Img, imgUrl) = it.base64Img to it.imgUrl

                        val imageUri = when {
                            base64Img != null -> Sponsor.ImageUri.Base64ImageUri(base64Img)
                            imgUrl != null -> Sponsor.ImageUri.NetworkImageUri(imgUrl)
                            else -> throw IllegalStateException("a json file is broken.")
                        }

                        Sponsor(it.link, imageUri)
                    })
                })
            }
        }.toFlowable()
}
