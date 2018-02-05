package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.model.Post
import java.util.Date

fun List<io.github.droidkaigi.confsched2018.data.api.response.Post>.toFeeds():
        List<Post> = map {
    Post(
            title = it.title!!,
            content = it.content!!,
            date = Date(it.date!!.time),
            published = it.published!!,
            type = it.type!!.let {
                when (it) {
                    "tutorial" -> Post.Type.TUTORIAL
                    "notification" -> Post.Type.NOTIFICATION
                    "alert" -> Post.Type.ALERT
                    "enquete" -> Post.Type.FEEDBACK
                    else -> throw IllegalStateException("unsupported Post type.")
                }
            }
    )
}
