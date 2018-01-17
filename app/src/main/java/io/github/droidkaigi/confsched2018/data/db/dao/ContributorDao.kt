package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity
import io.reactivex.Flowable

@Dao abstract class ContributorDao {
    @Query("SELECT * FROM contributor")
    abstract fun getAllContributors(): Flowable<List<ContributorEntity>>

    @Query("DELETE FROM contributor")
    abstract fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(contributors: List<ContributorEntity>)

    @Transaction open fun clearAndInsert(contributors: List<ContributorEntity>) {
        deleteAll()
        insert(contributors)
    }
}
