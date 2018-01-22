package io.github.droidkaigi.confsched2018.test.data.api

import com.squareup.moshi.Moshi
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.response.Response
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.api.response.mapper.ApplicationJsonAdapterFactory
import io.github.droidkaigi.confsched2018.data.api.response.mapper.LocalDateTimeAdapter
import io.reactivex.Single
import org.threeten.bp.LocalDateTime

class StubDroidKaigiApi : DroidKaigiApi {
    override fun getSessions(): Single<Response> = Single.create { emitter ->
        val json = StubDroidKaigiApi::class.java.classLoader.getResourceAsStream("all.json")
                .bufferedReader().use { it.readText() }
        val moshi = Moshi.Builder()
                .add(ApplicationJsonAdapterFactory.INSTANCE)
                .add(LocalDateTime::class.java, LocalDateTimeAdapter())
                .build()
        val adapter = moshi.adapter<Response>(Response::class.java)
        val response = adapter.fromJson(json)
        emitter.onSuccess(response!!)
    }

    override fun sponsorPlansJa(): Single<List<SponsorPlan>> {
        throw NotImplementedError()
    }

    override fun sponsorPlansEn(): Single<List<SponsorPlan>> {
        throw NotImplementedError()
    }
}
