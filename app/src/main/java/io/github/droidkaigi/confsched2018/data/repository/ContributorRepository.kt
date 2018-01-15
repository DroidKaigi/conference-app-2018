package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.model.Contributor
import io.reactivex.Completable
import io.reactivex.Flowable

interface ContributorRepository {
    val contributors: Flowable<List<Contributor>>
    @CheckResult fun loadContributors(): Completable
}
