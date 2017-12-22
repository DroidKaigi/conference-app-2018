package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.github.droidkaigi.confsched2018.data.db.entity.SessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.reactivex.Flowable
import org.intellij.lang.annotations.Language

@Dao abstract class SessionSpeakerJoinDao {
    @Language("RoomSql")
    @Transaction
    @Query("SELECT * FROM session")
    abstract fun getAllSessions(): Flowable<List<SessionWithSpeakers>>

    @Insert abstract fun insert(sessionSpeakerJoin: List<SessionSpeakerJoinEntity>)
}
