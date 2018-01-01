package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionEntity
import io.reactivex.Flowable

@Dao abstract class SessionDao {
    @Query("SELECT * FROM session")
    abstract fun getAllSessions(): Flowable<List<SessionEntity>>

    @Query("DELETE FROM session")
    abstract fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(sessions: List<SessionEntity>)

    @Transaction open fun clearAndInsert(newSessions: List<SessionEntity>) {
        deleteAll()
        insert(newSessions)
    }

    @Query("SELECT room_id, room_name FROM session GROUP BY room_id ORDER BY room_id")
    abstract fun getAllRoom(): Flowable<List<RoomEntity>>
}
