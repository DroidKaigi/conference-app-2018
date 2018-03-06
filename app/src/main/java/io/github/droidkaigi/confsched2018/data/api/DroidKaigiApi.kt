package io.github.droidkaigi.confsched2018.data.api

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.response.Response
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface DroidKaigiApi {
    @GET("sessionize/all.json")
    @CheckResult
    fun getSessions(): Deferred<Response>
}
