package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.*
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.reactivex.Flowable

@Dao abstract class SpeakerDao {
    @Query("SELECT * FROM speaker")
    abstract fun getAllSpeaker(): Flowable<List<SpeakerEntity>>

    @Query("DELETE FROM speaker")
    abstract fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(speakers: List<SpeakerEntity>)

    @Transaction open fun clearAndInsert(newSessions: List<SpeakerEntity>) {
        deleteAll()
        insert(newSessions)
    }

}
