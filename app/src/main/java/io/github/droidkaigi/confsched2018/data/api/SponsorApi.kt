package io.github.droidkaigi.confsched2018.data.api

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.reactivex.Single
import retrofit2.http.GET

interface SponsorApi {
    @GET("/jmatsu/0025d16eb276bcafaf1a75760b38e265/raw/f7f0e7d9cd0669349d1a4698ae875a683bdbac70/sponsors.json")
    @CheckResult
    fun sponsorPlans(): Single<List<SponsorPlan>>
}
