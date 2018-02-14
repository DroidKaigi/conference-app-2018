package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.annotation.SuppressLint
import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionFeedbackEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.model.SessionMessage
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.util.ext.atJST
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import java.util.Date

fun SessionWithSpeakers.toSession(
        speakerEntities: List<SpeakerEntity>,
        favList: List<Int>?,
        feedbacks: List<SessionFeedbackEntity>,
        firstDay: LocalDate
): Session.SpeechSession {
    val sessionEntity = session!!
    require(speakerIdList.isNotEmpty())
    val speakers = speakerIdList.map { speakerId ->
        val speakerEntity = speakerEntities.first { it.id == speakerId }
        speakerEntity.toSpeaker()
    }
    require(speakers.isNotEmpty())
    return Session.SpeechSession(
            id = sessionEntity.id,
            // dayNumber is starts with 1. Example: First day = 1, Second day = 2. So I plus 1 to period days
            dayNumber = Period.between(
                    firstDay, sessionEntity.stime.atJST().toLocalDate()).days + 1,
            startTime = Date(sessionEntity.stime.toEpochMilli()),
            endTime = Date(sessionEntity.etime.toEpochMilli()),
            title = sessionEntity.title,
            desc = sessionEntity.desc,
            room = Room(sessionEntity.room.id, sessionEntity.room.name),
            format = sessionEntity.sessionFormat,
            language = sessionEntity.language,
            topic = Topic(sessionEntity.topic.id, sessionEntity.topic.name),
            level = Level.of(sessionEntity.level.id, sessionEntity.level.name),
            isFavorited = favList!!.map { it.toString() }.contains(sessionEntity.id),
            speakers = speakers,
            feedback = feedbacks
                    .firstOrNull { it.sessionId == sessionEntity.id }
                    ?.toSessionFeedback()
                    ?: SessionFeedback(sessionEntity.id, 0, 0, 0, 0, 0, "", false),
            message = if (sessionEntity.message == null) {
                null
            } else {
                SessionMessage(sessionEntity.message.ja, sessionEntity.message.en)
            }
    )
}

fun Session.toSchedule(): SessionSchedule {
    return SessionSchedule(
            dayNumber = dayNumber,
            startTime = startTime
    )
}

fun SessionFeedbackEntity.toSessionFeedback(): SessionFeedback = SessionFeedback(
        sessionId = sessionId,
        totalEvaluation = totalEvaluation,
        relevancy = relevancy,
        asExpected = asExpected,
        difficulty = difficulty,
        knowledgeable = knowledgeable,
        comment = comment,
        submitted = submitted
)

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

@SuppressLint("VisibleForTests")
@CheckResult
fun Flowable<List<RoomEntity>>.toRooms(): Flowable<List<Room>> = map { roomEntities ->
    roomEntities.toRooms()
}

@SuppressLint("VisibleForTests")
@CheckResult
fun Flowable<List<TopicEntity>>.toTopics(): Flowable<List<Topic>> = map { topicEntities ->
    topicEntities.toTopics()
}

@VisibleForTesting
fun List<RoomEntity>.toRooms() =
        map { Room(it.id, it.name) }

@VisibleForTesting
fun List<TopicEntity>.toTopics() =
        map { Topic(it.id, it.name) }
