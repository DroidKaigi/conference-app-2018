package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.reactivex.Flowable

fun List<SessionWithSpeakers>.toSessions(speakerEntities: List<SpeakerEntity>, favList: List<Int>?): List<Session> =
        mapTo(arrayListOf<Session>()) { sessionEntity ->
            val session = sessionEntity.session!!
            Session(
                    id = session.id,
                    title = session.title,
                    desc = session.desc,
                    isFavorited = favList!!.map { it.toString() }.contains(session.id),
                    format = session.sessionFormat,
                    room = Room(session.room.name),
                    speakers = sessionEntity.speakerIdList.mapTo(arrayListOf<Speaker>()) { speakerId ->
                        val speakerEntity = speakerEntities.first { it.id == speakerId }
                        speakerEntity.toSpeaker()
                    }
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
