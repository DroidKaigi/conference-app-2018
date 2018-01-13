package io.github.droidkaigi.confsched2018.data.api

import io.github.droidkaigi.confsched2018.data.api.response.Response
import io.reactivex.Single
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface DroidKaigiApi {
    @GET("all.json")
    fun getSessions(): Single<Response>
}
