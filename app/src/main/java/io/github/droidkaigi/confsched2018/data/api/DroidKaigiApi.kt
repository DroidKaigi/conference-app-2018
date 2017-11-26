package io.github.droidkaigi.confsched2018.data.api

import io.github.droidkaigi.confsched2018.data.entity.SessionEntity
import io.reactivex.Single
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface DroidKaigiApi {
    @GET("sessions.json")
    fun getSessions(): Single<List<SessionEntity>>
}
