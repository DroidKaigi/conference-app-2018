package io.github.droidkaigi.confsched2018.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanWithSponsor
import io.reactivex.Flowable

@Dao abstract class SponsorDao {

    @Insert
    abstract fun insertSponsorPlan(plans: List<SponsorPlanEntity>)

    @Insert
    abstract fun insertSponsor(sponsors: List<SponsorEntity>)

    @Query("DELETE FROM sponsor_plan")
    abstract fun deleteAll()

    @Transaction
    fun clearAndInsert(plans: List<SponsorPlanEntity>, sponsors: List<SponsorEntity>) {
        deleteAll()
        insertSponsorPlan(plans)
        insertSponsor(sponsors)
    }

    @Query("SELECT * FROM sponsor_plan")
    abstract fun getAllSponsorPlan(): Flowable<List<SponsorPlanWithSponsor>>

}
