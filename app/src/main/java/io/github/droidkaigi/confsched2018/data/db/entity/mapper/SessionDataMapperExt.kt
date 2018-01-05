package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.model.parseDate
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId

fun SessionWithSpeakers.toSession(
        speakerEntities: List<SpeakerEntity>,
        favList: List<Int>?,
        firstDay: LocalDate
): Session {
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
            // dayNumber is starts with 1. Example: First day = 1, Second day = 2. So I plus 1 to period days
            dayNumber = Period.between(firstDay, sessionEntity.stime.toLocalDate()).days + 1,
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
        id = id,
        name = name,
        tagLine = tagLine,
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
