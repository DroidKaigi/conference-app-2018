package io.github.droidkaigi.confsched2018.data.db

import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity
import io.reactivex.Flowable

interface ContributorDatabase {
    fun getAll(): Flowable<List<ContributorEntity>>
    fun save(contributors: List<Contributor>)
}
