package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.RoomDatabase
import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.github.droidkaigi.confsched2018.data.api.response.mapper.toEntities
import io.github.droidkaigi.confsched2018.data.db.dao.ContributorDao
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity
import io.reactivex.Flowable
import javax.inject.Inject

class ContributorRoomDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val dao: ContributorDao
) : ContributorDatabase {
    override fun getAll(): Flowable<List<ContributorEntity>> =
            dao.getAllContributors()

    override fun save(contributors: List<Contributor>) {
        database.runInTransaction {
            dao.clearAndInsert(contributors.toEntities())
        }
    }
}
