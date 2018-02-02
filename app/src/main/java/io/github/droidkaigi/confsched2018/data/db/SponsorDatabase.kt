package io.github.droidkaigi.confsched2018.data.db

import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.reactivex.Flowable

interface SponsorDatabase {

    fun getSponsors(): Flowable<List<SponsorEntity>>

    fun save(sponsorEntities: List<SponsorEntity>)
}
