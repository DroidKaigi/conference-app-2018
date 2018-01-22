package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.github.droidkaigi.confsched2018.model.Level
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.util.ext.toUnixMills
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import java.util.Date

fun SessionWithSpeakers.toSession(
        speakerEntities: List<SpeakerEntity>,
        favList: List<Int>?,
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
            dayNumber = Period.between(firstDay, sessionEntity.stime.toLocalDate()).days + 1,
            startTime = Date(sessionEntity.stime.toUnixMills()),
            endTime = Date(sessionEntity.etime.toUnixMills()),
            title = sessionEntity.title,
            desc = sessionEntity.desc,
            room = Room(sessionEntity.room.id, sessionEntity.room.name),
            format = sessionEntity.sessionFormat,
            language = sessionEntity.language,
            topic = Topic(sessionEntity.topic.id, sessionEntity.topic.name),
            level = Level.of(sessionEntity.level.id, sessionEntity.level.name),
            isFavorited = favList!!.map { it.toString() }.contains(sessionEntity.id),
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

@SuppressLint("VisibleForTests")
fun Flowable<List<RoomEntity>>.toRooms(): Flowable<List<Room>> = map { roomEntities ->
    roomEntities.toRooms()
}

@SuppressLint("VisibleForTests")
fun Flowable<List<TopicEntity>>.toTopics(): Flowable<List<Topic>> = map { topicEntities ->
    topicEntities.toTopics()
}

@VisibleForTesting
fun List<RoomEntity>.toRooms() =
        map { Room(it.id, it.name) }

@VisibleForTesting
fun List<TopicEntity>.toTopics() =
        map { Topic(it.id, it.name) }
