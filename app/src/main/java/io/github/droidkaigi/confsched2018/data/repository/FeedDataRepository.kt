package io.github.droidkaigi.confsched2018.data.repository

import io.github.droidkaigi.confsched2018.data.api.FeedApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toFeeds
import io.github.droidkaigi.confsched2018.model.Post
import io.reactivex.Flowable

class FeedDataRepository(feedApi: FeedApi) : FeedRepository {
    override val feeds: Flowable<List<Post>> =
            feedApi.feeds.map { it.toFeeds() }
}
