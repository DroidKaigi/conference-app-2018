package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Category
import io.github.droidkaigi.confsched2018.data.api.response.Room
import io.github.droidkaigi.confsched2018.data.api.response.Session
import io.github.droidkaigi.confsched2018.data.api.response.Speaker
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity

fun List<Session>?.toSessionSpeakerJoinEntity(): List<SessionSpeakerJoinEntity> {
    val sessionSpeakerJoinEntity: MutableList<SessionSpeakerJoinEntity> = arrayListOf()
    this!!.forEach { responseSession ->
        responseSession.speakers!!.forEach { speakerId ->
            sessionSpeakerJoinEntity += SessionSpeakerJoinEntity(responseSession.id!!, speakerId!!)
        }
    }
    return sessionSpeakerJoinEntity
}

fun List<Session>?.toSessionEntity(categories: List<Category>?, rooms: List<io.github.droidkaigi.confsched2018.data.api.response.Room>?): List<SessionEntity> =
        this!!.mapTo(arrayListOf()) { responseSession ->
            SessionEntity(id = responseSession.id!!,
                    title = responseSession.title!!,
                    sessionFormat = categories.categoryValueName(0, responseSession.categoryItems!![0]),
                    room = RoomEntity(rooms.roomName(responseSession.roomId))
            )
        }

fun List<Speaker>?.toSpeakerEntity(): List<SpeakerEntity> =
        this!!.mapTo(arrayListOf()) { responseSpeaker ->
            SpeakerEntity(id = responseSpeaker.id!!,
                    name = responseSpeaker.fullName!!
            )
        }

private fun List<Category>?.categoryValueName(categoryIndex: Int, categoryId: Int?): String =
        this!![categoryIndex].items!!.first { it!!.id == categoryId }!!.name!!

private fun List<Room>?.roomName(roomId: Int?): String =
        this!!.first { it.id == roomId }.name!!
