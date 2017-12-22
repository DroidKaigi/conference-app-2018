package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Category
import io.github.droidkaigi.confsched2018.data.api.response.Room
import io.github.droidkaigi.confsched2018.data.api.response.Session
import io.github.droidkaigi.confsched2018.data.api.response.Speaker
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity

fun List<Session>?.toSessionSpeakerJoinEntities(): List<SessionSpeakerJoinEntity> {
    val sessionSpeakerJoinEntity: MutableList<SessionSpeakerJoinEntity> = arrayListOf()
    this!!.forEach { responseSession ->
        responseSession.speakers!!.forEach { speakerId ->
            sessionSpeakerJoinEntity += SessionSpeakerJoinEntity(responseSession.id!!, speakerId!!)
        }
    }
    return sessionSpeakerJoinEntity
}

fun List<Session>?.toSessionEntities(categories: List<Category>?, rooms: List<io.github.droidkaigi.confsched2018.data.api.response.Room>?): List<SessionEntity> =
        this!!.map { responseSession ->
            responseSession.toSessionEntity(categories, rooms)
        }

fun Session.toSessionEntity(categories: List<Category>?, rooms: List<Room>?): SessionEntity {
    return SessionEntity(
            id = id!!,
            title = title!!,
            desc = description!!,
            stime = startsAt!!,
            etime = endsAt!!,
            sessionFormat = categories.categoryValueName(0, categoryItems!![0]),
            language = categories.categoryValueName(1, categoryItems[1]),
            topic = categories.categoryValueName(2, categoryItems[2]),
            level = categories.categoryValueName(3, categoryItems[3]),
            room = RoomEntity(rooms.roomName(roomId))
    )
}

fun List<Speaker>?.toSpeakerEntities(): List<SpeakerEntity> =
        this!!.map { responseSpeaker ->
            SpeakerEntity(id = responseSpeaker.id!!,
                    name = responseSpeaker.fullName!!,
                    imageUrl = responseSpeaker.profilePicture.orEmpty(),
                    twitterUrl = responseSpeaker.links
                            ?.firstOrNull { "Twitter" == it?.linkType }
                            ?.url,
                    companyUrl = responseSpeaker.links
                            ?.firstOrNull { "Company_Website" == it?.linkType }
                            ?.url,
                    blogUrl = responseSpeaker.links
                            ?.firstOrNull { "Blog" == it?.linkType }
                            ?.url,
                    githubUrl = responseSpeaker.links
                            ?.firstOrNull { "GitHub" == it?.title || "Github" == it?.title }
                            ?.url
            )
        }

private fun List<Category>?.categoryValueName(categoryIndex: Int, categoryId: Int?): String =
        this!![categoryIndex].items!!.first { it!!.id == categoryId }!!.name!!

private fun List<Room>?.roomName(roomId: Int?): String =
        this!!.first { it.id == roomId }.name!!
