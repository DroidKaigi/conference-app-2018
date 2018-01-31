package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.RoomDatabase
import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.response.Response
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionFeedbackEntity
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSessionSpeakerJoinEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSpeakerEntities
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionFeedbackDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionFeedbackEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.reactivex.Flowable
import javax.inject.Inject

class SessionRoomDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val sessionDao: SessionDao,
        private val speakerDao: SpeakerDao,
        private val sessionSpeakerJoinDao: SessionSpeakerJoinDao,
        private val sessionFeedbackDao: SessionFeedbackDao
) : SessionDatabase {
    @CheckResult override fun getAllSessions(): Flowable<List<SessionWithSpeakers>> =
            sessionSpeakerJoinDao.getAllSessions()

    @CheckResult override fun getAllSessionFeedback(): Flowable<List<SessionFeedbackEntity>> =
            sessionFeedbackDao.getAllSessionFeedback()

    @CheckResult
    override fun getAllSpeaker(): Flowable<List<SpeakerEntity>> = speakerDao.getAllSpeaker()

    @CheckResult override fun getAllRoom(): Flowable<List<RoomEntity>> = sessionDao.getAllRoom()

    @CheckResult override fun getAllTopic(): Flowable<List<TopicEntity>> = sessionDao.getAllTopic()

    override fun save(response: Response) {
        database.runInTransaction {
            speakerDao.clearAndInsert(response.speakers.toSpeakerEntities())
            val sessions = response.sessions
            val sessionEntities = sessions.toSessionEntities(response.categories, response.rooms)
            sessionDao.clearAndInsert(sessionEntities)
            sessionSpeakerJoinDao.insert(sessions.toSessionSpeakerJoinEntities())
        }
    }

    override fun saveSessionFeedback(sessionFeedback: SessionFeedback) {
        database.runInTransaction {
            sessionFeedbackDao.upsert(sessionFeedback.toSessionFeedbackEntity())
        }
    }
}
