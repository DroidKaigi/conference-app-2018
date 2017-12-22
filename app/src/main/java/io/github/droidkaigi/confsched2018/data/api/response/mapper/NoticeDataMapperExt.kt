package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.model.Post
import io.github.droidkaigi.confsched2018.model.parseDate

fun List<io.github.droidkaigi.confsched2018.data.api.response.Post>.toFeeds():
        List<Post> = map {
    Post(
            title = it.title!!,
            content = it.content!!,
            date = parseDate(it.date!!.time),
            published = it.published!!,
            type = it.type!!
    )
}
