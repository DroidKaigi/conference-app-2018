package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.github.droidkaigi.confsched2018.data.db.entity.SessionFeedbackEntity
import io.reactivex.Flowable

@Dao abstract class SessionFeedbackDao {
    @Query("SELECT * FROM session_feedback")
    abstract fun getAllSessionFeedback(): Flowable<List<SessionFeedbackEntity>>

    @Query("DELETE FROM session_feedback WHERE session_id = :sessionId")
    abstract fun delete(sessionId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(sessionFeedback: SessionFeedbackEntity)
}
