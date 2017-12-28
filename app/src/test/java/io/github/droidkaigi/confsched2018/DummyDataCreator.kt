package io.github.droidkaigi.confsched2018

import io.github.droidkaigi.confsched2018.model.*

val DUMMY_SESSION_ID_1 = "test1"
val DUMMY_SESSION_ID_2 = "test2"

fun createDummySessions(): List<Session> =
        listOf(createDummySession(DUMMY_SESSION_ID_1), createDummySession(DUMMY_SESSION_ID_2))

fun createDummySession(sessionId: String): Session = Session(
        id = sessionId,
        title = "DroidKaigi",
        desc = "How to create DroidKaigi app",
        startTime = parseDate(10000),
        endTime = parseDate(10000),
        format = "30分",
        room = Room(1, "Hall"),
        topic = Topic(2, "Development tool"),
        language = "JA",
        level = Level(1, "Beginner"),
        speakers = listOf(
                createSpeaker(),
                createSpeaker()
        ),
        isFavorited = true
)

private fun createSpeaker(): Speaker {
    return Speaker(
            name = "tm",
            imageUrl = "http://example.com",
            twitterUrl = "http://twitter.com/",
            githubUrl = null,
            blogUrl = null,
            companyUrl = null
    )
}
