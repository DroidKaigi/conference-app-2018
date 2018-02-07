package io.github.droidkaigi.confsched2018.data.repository

import android.support.annotation.CheckResult
import io.github.droidkaigi.confsched2018.data.api.GithubApi
import io.github.droidkaigi.confsched2018.data.db.ContributorDatabase
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.toContributors
import io.github.droidkaigi.confsched2018.data.api.response.Contributor as ContributorResponse
import io.github.droidkaigi.confsched2018.model.Contributor
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.zipWith
import timber.log.Timber
import javax.inject.Inject

class ContributorDataRepository @Inject constructor(
        private val api: GithubApi,
        private val contributorDatabase: ContributorDatabase,
        private val schedulerProvider: SchedulerProvider
) : ContributorRepository {
    @CheckResult override fun loadContributors(): Completable {
        // We want to implement paging logic,
        // But The GitHub API does not return the total count of contributors in response data.
        // And we want to show total count.
        // So we fetch all contributors when first time.
        return (1..MAX_PAGE).map { page ->
            api.getContributors(OWNER, REPO, MAX_PER_PAGE, page)
        }.reduce { acc, single ->
                    acc.zipWith(
                            single,
                            { page1: List<ContributorResponse>, page2: List<ContributorResponse> ->
                                page1 + page2
                            }
                    )
                }.doOnSuccess {
                    if (DEBUG) it.forEach { Timber.d("$it") }
                    contributorDatabase.save(it)
                }
                .subscribeOn(schedulerProvider.io())
                .toCompletable()
    }

    override val contributors: Flowable<List<Contributor>> =
            contributorDatabase.getAll()
                    .map { it.toContributors() }
                    .subscribeOn(schedulerProvider.io())

    companion object {
        private const val OWNER = "DroidKaigi"
        private const val REPO = "conference-app-2018"
        private const val MAX_PER_PAGE = 100
        // Max page num of github api for contributors.
        // If the number of the contributors will be over 200, this number must be changed to 3.
        private const val MAX_PAGE = 2
        private const val DEBUG = false
    }
}
