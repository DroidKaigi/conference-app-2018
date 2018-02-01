package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Category
import io.github.droidkaigi.confsched2018.data.api.response.CategoryItem
import io.github.droidkaigi.confsched2018.data.api.response.Room
import io.github.droidkaigi.confsched2018.data.api.response.Session
import io.github.droidkaigi.confsched2018.data.api.response.Speaker
import io.github.droidkaigi.confsched2018.data.db.entity.LevelEntity
import io.github.droidkaigi.confsched2018.data.db.entity.MessageEntity
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionFeedbackEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.github.droidkaigi.confsched2018.model.SessionFeedback

fun List<Session>?.toSessionSpeakerJoinEntities(): List<SessionSpeakerJoinEntity> {
    val sessionSpeakerJoinEntity: MutableList<SessionSpeakerJoinEntity> = arrayListOf()
    this!!.forEach { responseSession ->
        responseSession.speakers!!.forEach { speakerId ->
            sessionSpeakerJoinEntity +=
                    SessionSpeakerJoinEntity(responseSession.id, speakerId!!)
        }
    }
    return sessionSpeakerJoinEntity
}

fun SessionFeedback.toSessionFeedbackEntity(): SessionFeedbackEntity {
    return SessionFeedbackEntity(
            sessionId = sessionId,
            totalEvaluation = totalEvaluation,
            relevancy = relevancy,
            asExpected = asExpected,
            difficulty = difficulty,
            knowledgeable = knowledgeable,
            comment = comment,
            submitted = submitted
    )
}

fun List<Session>?.toSessionEntities(
        categories: List<Category>?,
        rooms: List<Room>?
): List<SessionEntity> =
        this!!.map {
            it.toSessionEntity(categories, rooms)
        }

fun Session.toSessionEntity(categories: List<Category>?, rooms: List<Room>?): SessionEntity {
    val sessionFormat = categories.category(0, categoryItems!![0])
    val language = categories.category(1, categoryItems[1])
    val topic = categories.category(2, categoryItems[2])
    val level = categories.category(3, categoryItems[3])
    return SessionEntity(
            id = id,
            title = title!!,
            desc = description!!,
            stime = startsAt!!,
            etime = endsAt!!,
            sessionFormat = sessionFormat.name!!,
            language = language.name!!,
            message = if (message != null) {
                MessageEntity(message.ja!!, message.en!!)
            } else {
                null
            },
            topic = TopicEntity(topic.id!!, topic.name!!),
            level = LevelEntity(level.id!!, level.name!!),
            room = RoomEntity(roomId!!, rooms.roomName(roomId))
    )
}

fun List<Speaker>?.toSpeakerEntities(): List<SpeakerEntity> =
        this!!.map { responseSpeaker ->
            SpeakerEntity(id = responseSpeaker.id!!,
                    name = responseSpeaker.fullName!!,
                    tagLine = responseSpeaker.tagLine!!,
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

private fun List<Category>?.category(categoryIndex: Int, categoryId: Int?): CategoryItem =
        this!![categoryIndex].items!!.first { it!!.id == categoryId }!!

private fun List<Room>?.roomName(roomId: Int?): String =
        this!!.first { it.id == roomId }.name!!
