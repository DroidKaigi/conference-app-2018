package io.github.droidkaigi.confsched2018.data.repository

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_TITLE1
import io.github.droidkaigi.confsched2018.createDummySessionFeedbackEntities
import io.github.droidkaigi.confsched2018.createDummySessionWithSpeakersEntities
import io.github.droidkaigi.confsched2018.createDummySpeakerEntities
import io.github.droidkaigi.confsched2018.createDummySpeakerEntry1
import io.github.droidkaigi.confsched2018.createDummySpeakerEntry2
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toRooms
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSpeaker
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toTopics
import io.github.droidkaigi.confsched2018.data.db.fixeddata.SpecialSessions
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.threeten.bp.LocalDate

@RunWith(RobolectricTestRunner::class)
class SessionsDataRepositoryTest {
    private val sessionDatabase: SessionDatabase = mock()
    private val favoriteDatabase: FavoriteDatabase = mock()

    @Before fun init() {
        whenever(sessionDatabase.getAllRoom()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllTopic()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(mock()))
        whenever(favoriteDatabase.favorites).doReturn(Flowable.just(emptyList()))
    }

    @Test fun rooms() {
        val rooms = listOf(RoomEntity(1, "A"), RoomEntity(2, "B"))
        whenever(sessionDatabase.getAllRoom()).doReturn(Flowable.just(rooms))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository
                .rooms
                .test()
                .assertValue(rooms.toRooms())

        verify(sessionDatabase).getAllRoom()
    }

    @Test fun topics() {
        val topics = listOf(TopicEntity(1, "topic_a"), TopicEntity(2, "topic_b"))
        whenever(sessionDatabase.getAllTopic()).doReturn(Flowable.just(topics))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository
                .topics
                .test()
                .assertValue(topics.toTopics())

        verify(sessionDatabase).getAllTopic()
    }

    @Test fun sessions() {
        val sessions = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        val feedbacks = createDummySessionFeedbackEntities()

        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessions))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(feedbacks))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository
                .sessions
                .test()
                .assertValueAt(0,
                        sessions.map {
                            it.toSession(speakers,
                                    listOf(),
                                    createDummySessionFeedbackEntities(),
                                    LocalDate.of(1, 1, 1))
                        }
                                + SpecialSessions.getSessions()
                )

        verify(sessionDatabase).getAllSessions()
    }

    @Test fun sessionFeedbacks() {
        val sessionEntities = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        val sessionFeedbacks = createDummySessionFeedbackEntities()

        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessionEntities))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(sessionFeedbacks))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository.sessions
                .test()
                .assertNoErrors()
                .assertValue(
                        sessionEntities.map { sessionEntity ->
                            sessionEntity.toSession(speakers, emptyList(), sessionFeedbacks,
                                    LocalDate.of(1, 1, 1))
                        } + sessionDataRepository.specialSessions)

        verify(sessionDatabase).getAllSessionFeedback()
    }

    @Test fun search() {
        val sessions = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        val feedbacks = createDummySessionFeedbackEntities()
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessions))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(feedbacks))
        val sessionDataRepository = SessionDataRepository(
                mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider()
        )

        sessionDataRepository.search(DUMMY_SESSION_TITLE1)
                .test()
                .assertValue(SearchResult(listOf(
                        sessions[0]
                                .toSession(
                                        speakers,
                                        emptyList(),
                                        feedbacks,
                                        LocalDate.of(1, 1, 1)
                                )
                ), listOf()))

        verify(sessionDatabase).getAllSessions()
    }

    @Test fun roomSessions() {
        val sessionEntities = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        val feedbacks = createDummySessionFeedbackEntities()
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessionEntities))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(feedbacks))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())
        val session1 = sessionEntities[0].toSession(
                speakers,
                emptyList(),
                feedbacks,
                LocalDate.of(1, 1, 1)
        )
        val session2 = sessionEntities[1].toSession(
                speakers,
                emptyList(),
                feedbacks,
                LocalDate.of(1, 1, 1))

        val specialSessions = SpecialSessions.getSessions()
        sessionDataRepository.roomSessions
                .test()
                .assertNoErrors()
                .assertValueAt(
                        0,
                        mapOf(
                                session1.room to listOf(session1, session2),
                                specialSessions[0].room!! to specialSessions.filter { it.room != null }
                        )
                )

        verify(sessionDatabase).getAllSessions()
    }

    @Test fun speakerSessions() {
        val sessionEntities = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        val feedbacks = createDummySessionFeedbackEntities()
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessionEntities))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        whenever(sessionDatabase.getAllSessionFeedback()).doReturn(Flowable.just(feedbacks))
        val sessionDataRepository = SessionDataRepository(mock(),
                mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())
        val sessions1 = listOf(
                sessionEntities[0].toSession(
                        speakers,
                        emptyList(),
                        feedbacks,
                        LocalDate.of(1, 1, 1)
                ))
        val sessions2 = sessions1 + listOf(sessionEntities[1].toSession(
                speakers,
                emptyList(),
                feedbacks,
                LocalDate.of(1, 1, 1)
        ))

        sessionDataRepository.speakerSessions
                .test()
                .assertNoErrors()
                .assertValueAt(
                        0,
                        mapOf(
                                createDummySpeakerEntry1().toSpeaker() to sessions2,
                                createDummySpeakerEntry2().toSpeaker() to sessions1
                        )
                )

        verify(sessionDatabase).getAllSessions()
    }
}
