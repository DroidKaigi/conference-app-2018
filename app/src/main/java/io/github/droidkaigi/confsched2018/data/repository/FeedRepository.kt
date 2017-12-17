package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.model.Post
import io.reactivex.Flowable

interface FeedRepository {
    val feeds: Flowable<List<Post>>
}
