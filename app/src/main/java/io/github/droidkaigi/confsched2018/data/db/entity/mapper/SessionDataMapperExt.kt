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
            Session(
                    id = sessionEntity.session!!.id,
                    title = sessionEntity.session!!.title,
                    isFavorited = favList!!.map { it.toString() }.contains(sessionEntity.session!!.id),
                    format = sessionEntity.session!!.sessionFormat,
                    room = Room(sessionEntity.session!!.room.name),
                    speakers = sessionEntity.speakerIdList.mapTo(arrayListOf<Speaker>()) { speakerId ->
                        val speakerEntity = speakerEntities.first { it.id == speakerId }
                        Speaker(
                                name = speakerEntity.name,
                                imageUrl = speakerEntity.imageUrl,
                                twitterName = speakerEntity.twitterName,
                                githubName = speakerEntity.githubName
                        )

                    }
            )

        }

fun Flowable<List<RoomEntity>>.toRooms(): Flowable<List<Room>> {
    return map { roomEntities ->
        roomEntities.map { Room(it.name) }
    }
}
