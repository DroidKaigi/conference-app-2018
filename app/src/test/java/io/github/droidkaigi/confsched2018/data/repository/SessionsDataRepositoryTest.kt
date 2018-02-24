package io.github.droidkaigi.confsched2018.data.repository

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.createDummySessionWithSpeakersEntities
import io.github.droidkaigi.confsched2018.createDummySpeakerEntities
import io.github.droidkaigi.confsched2018.data.db.FavoriteDatabase
import io.github.droidkaigi.confsched2018.data.db.SessionDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toSession
import io.github.droidkaigi.confsched2018.data.db.fixeddata.SpecialSessions
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
        whenever(favoriteDatabase.favorites).doReturn(Flowable.just(emptyList()))
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
                .assertValueAt(0,
                        sessions.map {
                            it.toSession(speakers,
                                    listOf(),
                                    LocalDate.of(1, 1, 1))
                        }
                                + SpecialSessions.getSessions()
                )

        verify(sessionDatabase).getAllSessions()
    }
}
