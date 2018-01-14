package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.SponsorApi
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class SponsorPlanDataRepository @Inject constructor(
        private val sponsorApi: SponsorApi
) : SponsorPlanRepository {
    override val sponsorPlans: Flowable<List<SponsorPlan>>
        get() = sponsorApi.sponsorPlans().toFlowable().map {
            it.map {
                SponsorPlan(it.planName, when (it.planType) {
                    io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type.PLATINUM -> SponsorPlan.Type.Platinum
                    io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type.GOLD -> SponsorPlan.Type.Gold
                    io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type.SILVER -> SponsorPlan.Type.Silver
                    io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type.SUPPORTER -> SponsorPlan.Type.Supporter
                    io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan.Type.TECHNICAL_FOR_NETWORK -> SponsorPlan.Type.TechnicalForNetwork
                }, it.groups.map {
                    SponsorGroup(it.sponsors.map {
                        if (it.base64Img == it.imgUrl) {
                            Timber.d(it.link)
                        }
                        Sponsor(it.link, if (it.base64Img == null) Sponsor.ImageUri.NetworkImageUri(it.imgUrl!!) else Sponsor.ImageUri.Base64ImageUri(it.base64Img!!))
                    })
                })
            }
        }
}
