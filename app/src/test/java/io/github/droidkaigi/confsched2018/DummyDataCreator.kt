package io.github.droidkaigi.confsched2018

import io.github.droidkaigi.confsched2018.data.db.entity.*
import io.github.droidkaigi.confsched2018.model.*
import org.threeten.bp.LocalDateTime

const val DUMMY_SESSION_ID1 = "test1"
const val DUMMY_SESSION_ID2 = "test2"
const val DUMMY_SESSION_TITLE1 = "DroidKaigi"
const val DUMMY_SESSION_TITLE2 = "RejectKaigi"

fun createDummySessions(): List<Session> =
        listOf(
                createDummySession(DUMMY_SESSION_ID1, DUMMY_SESSION_TITLE1),
                createDummySession(DUMMY_SESSION_ID2, DUMMY_SESSION_TITLE2)
        )

fun createDummySession(sessionId: String = DUMMY_SESSION_ID1, title: String = DUMMY_SESSION_TITLE1): Session {
    return Session(
            id = sessionId,
            title = title,
            desc = "How to create DroidKaigi app",
            startTime = parseDate(10000),
            endTime = parseDate(10000),
            format = "30分",
            room = Room(1, "Hall"),
            topic = Topic(2, "Development tool"),
            language = "JA",
            level = Level(1, "Beginner"),
            speakers = listOf(
                    createDummySpeaker(),
                    createDummySpeaker()
            ),
            isFavorited = true
    )
}

fun createDummySpeaker(): Speaker {
    return Speaker(
            id = "tmtm",
            name = "tm",
            imageUrl = "http://example.com",
            twitterUrl = "http://twitter.com/",
            githubUrl = null,
            blogUrl = null,
            companyUrl = null
    )
}


fun createDummySpeakerEntities(): List<SpeakerEntity> {
    return listOf(
            SpeakerEntity(
                    "aaaa"
                    , "hogehoge"
                    , "https://example.com"
                    , "http://example.com/hoge"
                    , null
                    , null
                    , "http://example.github.com/hoge"
            ),
            SpeakerEntity(
                    "bbbb"
                    , "hogehuga"
                    , "https://example.com"
                    , "http://example.com/hoge"
                    , null
                    , null
                    , "http://example.github.com/hoge"
            ))
}

fun createDummySessionWithSpeakersEntities(): List<SessionWithSpeakers> {
    return listOf(SessionWithSpeakers(SessionEntity(DUMMY_SESSION_ID1,
            DUMMY_SESSION_TITLE1,
            "Endless battle",
            LocalDateTime.of(1, 1, 1, 1, 1),
            LocalDateTime.of(1, 1, 1, 1, 1),
            "30分",
            "日本語",
            LevelEntity(1, "ニッチ / Niche"),
            TopicEntity(1, "開発環境 / Development"),
            RoomEntity(1, "ホール")),
            listOf("aaaa", "bbbb")),
            SessionWithSpeakers(SessionEntity(DUMMY_SESSION_ID2,
                    DUMMY_SESSION_TITLE2,
                    "Endless battle",
                    LocalDateTime.of(1, 1, 1, 1, 1),
                    LocalDateTime.of(1, 1, 1, 1, 1),
                    "30分",
                    "日本語",
                    LevelEntity(1, "ニッチ / Niche"),
                    TopicEntity(1, "開発環境 / Development"),
                    RoomEntity(1, "ホール")),
                    listOf("aaaa", "bbbb"))
    )
}
