package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.RoomDatabase
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorGroupEntities
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toSponsorPlanEntities
import io.github.droidkaigi.confsched2018.data.db.dao.SponsorDao
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanWithSponsor
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import io.reactivex.Flowable
import javax.inject.Inject

class SponsorRoomDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val sponsorDao: SponsorDao
) : SponsorDatabase {

    override fun getAllSponsorPlan():
            Flowable<List<Pair<SponsorPlanEntity, List<SponsorPlanWithSponsor>>>> {
        return sponsorDao.getAllSponsorPlan()
                .map { sponsorPlans ->
                    sponsorPlans.map { it to sponsorDao.getSponsors(it.id) }
                }
    }

    override fun save(plans: List<SponsorPlan>) {
        database.runInTransaction {
            sponsorDao.deleteAll()
            val planIds = sponsorDao.insertSponsorPlan(plans.toSponsorPlanEntities())
            plans.map { it.groups }.zip(planIds)
                    .forEach { (groups, planId) ->
                        val groupEntities = groups.toSponsorGroupEntities(planId.toInt())
                        val groupIds = sponsorDao.insertSponsorGroup(groupEntities)
                        groups.map { it.sponsors }.zip(groupIds).forEach { (sponsors, groupId) ->
                            sponsorDao.insertSponsor(sponsors.toSponsorEntities(groupId.toInt()))
                        }
                    }
        }
    }
}
