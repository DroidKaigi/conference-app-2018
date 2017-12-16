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

fun List<SessionWithSpeakers>.toSessions(speakerEntities: List<SpeakerEntity>, favList: List<Int>?): List<Session> =
        map { sessionWithSpeaker ->
            val session = sessionWithSpeaker.session!!
            require(!sessionWithSpeaker.speakerIdList.isEmpty())
            val speakers = sessionWithSpeaker.speakerIdList.map { speakerId ->
                val speakerEntity = speakerEntities.first { it.id == speakerId }
                speakerEntity.toSpeaker()
            }
            require(!speakers.isEmpty())
            Session(
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
                    speakers = speakers
            )

        }

fun SpeakerEntity.toSpeaker(): Speaker = Speaker(
        name = name,
        imageUrl = imageUrl,
        twitterName = twitterName,
        githubName = githubName
)

fun Flowable<List<RoomEntity>>.toRooms(): Flowable<List<Room>> = map { roomEntities ->
    roomEntities.map { Room(it.name) }
}
