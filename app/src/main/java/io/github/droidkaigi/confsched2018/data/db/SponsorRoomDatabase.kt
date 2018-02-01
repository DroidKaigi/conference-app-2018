package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.RoomDatabase
import io.github.droidkaigi.confsched2018.data.db.dao.SponsorDao
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.reactivex.Flowable
import javax.inject.Inject

class SponsorRoomDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val sponsorDao: SponsorDao
) : SponsorDatabase {

    override fun getSponsors(): Flowable<List<SponsorEntity>> {
        return sponsorDao.getAllSponsors()
    }

    override fun save(sponsorEntities: List<SponsorEntity>) {
        database.runInTransaction {
            sponsorDao.deleteAll()
            sponsorDao.insertSponsor(sponsorEntities)
        }
    }
}
