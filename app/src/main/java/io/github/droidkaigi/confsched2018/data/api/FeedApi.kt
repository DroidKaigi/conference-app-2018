package io.github.droidkaigi.confsched2018.data.api

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.response.Post
import io.reactivex.Flowable

interface FeedApi {
    @get:CheckResult
    val feeds: Flowable<List<Post>>
}
