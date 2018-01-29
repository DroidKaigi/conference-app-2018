package io.github.droidkaigi.confsched2018.data.api

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.response.Response
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.reactivex.Single
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface DroidKaigiApi {
    @GET("sessionize/all.json")
    fun getSessions(): Single<Response>

    @GET("sponsors/ja.json")
    @CheckResult
    fun sponsorPlansJa(): Single<List<SponsorPlan>>

    @GET("sponsors/en.json")
    @CheckResult
    fun sponsorPlansEn(): Single<List<SponsorPlan>>
}
