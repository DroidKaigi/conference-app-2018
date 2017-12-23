package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.parseDate
import io.reactivex.Flowable
import org.threeten.bp.ZoneId

fun SessionWithSpeakers.toSession(speakerEntities: List<SpeakerEntity>, favList: List<Int>?): Session {
    val session = session!!
    require(!speakerIdList.isEmpty())
    val speakers = speakerIdList.map { speakerId ->
        val speakerEntity = speakerEntities.first { it.id == speakerId }
        speakerEntity.toSpeaker()
    }
    require(!speakers.isEmpty())
    return Session(
            id = session.id,
            title = session.title,
            desc = session.desc,
            startTime = parseDate(session.stime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()),
            endTime = parseDate(session.etime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()),
            isFavorited = favList!!.map { it.toString() }.contains(session.id),
            format = session.sessionFormat,
            room = Room(session.room.name),
            level = session.level,
            language = session.language,
            topic = session.topic,
            sessionFormat = session.sessionFormat,
            speakers = speakers
    )
}

fun SpeakerEntity.toSpeaker(): Speaker = Speaker(
        name = name,
        imageUrl = imageUrl,
        twitterUrl = twitterUrl,
        companyUrl = companyUrl,
        blogUrl = blogUrl,
        githubUrl = githubUrl
)

fun Flowable<List<RoomEntity>>.toRooms(): Flowable<List<Room>> = map { roomEntities ->
    roomEntities.map { Room(it.name) }
}
