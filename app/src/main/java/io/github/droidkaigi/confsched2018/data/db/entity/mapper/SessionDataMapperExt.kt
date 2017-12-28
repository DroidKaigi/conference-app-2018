package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.model.*
import io.reactivex.Flowable
import org.threeten.bp.ZoneId

fun SessionWithSpeakers.toSession(speakerEntities: List<SpeakerEntity>, favList: List<Int>?): Session {
    val sessionEntity = session!!
    require(!speakerIdList.isEmpty())
    val speakers = speakerIdList.map { speakerId ->
        val speakerEntity = speakerEntities.first { it.id == speakerId }
        speakerEntity.toSpeaker()
    }
    require(!speakers.isEmpty())
    return Session(
            id = sessionEntity.id,
            title = sessionEntity.title,
            desc = sessionEntity.desc,
            startTime = parseDate(sessionEntity.stime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()),
            endTime = parseDate(sessionEntity.etime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()),
            isFavorited = favList!!.map { it.toString() }.contains(sessionEntity.id),
            format = sessionEntity.sessionFormat,
            room = Room(sessionEntity.room.id, sessionEntity.room.name),
            level = Level(sessionEntity.level.id, sessionEntity.level.name),
            language = sessionEntity.language,
            topic = Topic(sessionEntity.topic.id, sessionEntity.topic.name),
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
    roomEntities.toRooms()
}

@VisibleForTesting
fun List<RoomEntity>.toRooms() =
        map { Room(it.id, it.name) }
