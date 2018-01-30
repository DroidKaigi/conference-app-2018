package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.RoomDatabase
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorGroupEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorPlanEntities
import io.github.droidkaigi.confsched2018.data.db.dao.SponsorDao
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanWithSponsor
import io.reactivex.Flowable
import javax.inject.Inject

class SponsorRoomDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val sponsorDao: SponsorDao
) : SponsorDatabase {

    override fun getAllSponsorPlan(): Flowable<List<SponsorPlanWithSponsor>> {
        return sponsorDao.getAllSponsorPlan()
    }

    override fun save(plans: List<SponsorPlan>) {
        database.runInTransaction {
            sponsorDao.deleteAll()
            val planIds = sponsorDao.insertSponsorPlan(plans.toSponsorPlanEntities())
            plans.map { it.groups }.zip(planIds)
                    .forEach { (groups, planId) ->
                        val groupEntities = groups.toSponsorGroupEntities(planId.toInt())
                        sponsorDao.insertSponsor(groupEntities)
                    }
        }
    }
}
