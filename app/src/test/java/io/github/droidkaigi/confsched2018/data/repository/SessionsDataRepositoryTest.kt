package io.github.droidkaigi.confsched2018.data.repository

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_TITLE1
import io.github.droidkaigi.confsched2018.createDummySessionWithSpeakersEntities
import io.github.droidkaigi.confsched2018.createDummySpeakerEntities
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toRooms
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SessionsDataRepositoryTest {
    @Mock private val sessionDatabase: SessionDatabase = mock()
    @Mock private val favoriteDatabase: FavoriteDatabase = mock()

    @Before fun init() {
        whenever(sessionDatabase.getAllRoom()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(mock()))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(mock()))
        whenever(favoriteDatabase.favorites).doReturn(Flowable.just(mock()))
    }

    @Test fun rooms() {
        val rooms = listOf(RoomEntity(1, "A"), RoomEntity(2, "B"))
        whenever(sessionDatabase.getAllRoom()).doReturn(Flowable.just(rooms))
        val sessionDataRepository = SessionDataRepository(mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository
                .rooms
                .test()
                .assertValue(rooms.toRooms())

        verify(sessionDatabase).getAllRoom()
    }

    @Test fun sessions() {
        val sessions = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()

        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessions))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        val sessionDataRepository = SessionDataRepository(mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository
                .sessions
                .test()
                .assertValue(sessions.map { it.toSession(speakers, listOf()) })

        verify(sessionDatabase).getAllSessions()
    }

    @Test fun search() {
        val sessions = createDummySessionWithSpeakersEntities()
        val speakers = createDummySpeakerEntities()
        whenever(sessionDatabase.getAllSessions()).doReturn(Flowable.just(sessions))
        whenever(sessionDatabase.getAllSpeaker()).doReturn(Flowable.just(speakers))
        val sessionDataRepository = SessionDataRepository(mock(),
                sessionDatabase,
                favoriteDatabase,
                TestSchedulerProvider())

        sessionDataRepository.search(DUMMY_SESSION_TITLE1)
                .doOnSuccess {
                    println(it)
                }
                .test()
                .assertValue(SearchResult(listOf(sessions[0].toSession(speakers, emptyList())),
                        listOf()))

        verify(sessionDatabase).getAllSessions()
    }

}
