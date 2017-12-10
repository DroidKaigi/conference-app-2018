package io.github.droidkaigi.confsched2018.data.entity.mapper

import io.github.droidkaigi.confsched2018.data.entity.SessionEntity
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic

private fun SessionEntity.toSession(): Session {
    // FIXME: Null check when api start working
    return Session(
            id = id!!,
            title = title!!,
            desc = desc ?: "",
            speaker = Speaker(name = speaker?.name.orEmpty(),
                    imageUrl = speaker?.imageUrl.orEmpty(),
                    twitterName = speaker?.twitterName.orEmpty(),
                    githubName = speaker?.githubName.orEmpty()),
            stime = stime!!,
            etime = etime!!,
            durationMin = durationMin ?: 0,
            type = type!!,
            topic = Topic(
                    name = topic?.name.orEmpty(),
                    other = topic?.other.orEmpty()
            ),
            room = Room(room?.name.orEmpty()),
            lang = lang.orEmpty(),
            isFavorited = false
    )
}

fun List<SessionEntity>.toSession(likeList: List<Int>): List<Session> {
    return mapTo(arrayListOf()) {
        val session = it.toSession()
        session.isFavorited = likeList.contains(session.id)
        session
    }
}
