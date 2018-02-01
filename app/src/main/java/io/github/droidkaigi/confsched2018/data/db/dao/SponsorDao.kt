package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.reactivex.Flowable

@Dao
abstract class SponsorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSponsor(sponsors: List<SponsorEntity>)

    @Query("DELETE FROM sponsor")
    abstract fun deleteAll()

    @Query("SELECT * FROM sponsor")
    abstract fun getAllSponsors(): Flowable<List<SponsorEntity>>
}
