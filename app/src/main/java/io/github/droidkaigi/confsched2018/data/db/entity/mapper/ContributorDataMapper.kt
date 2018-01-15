package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity

fun ContributorEntity.toContributor(): Contributor = Contributor(
        name = name,
        bio = bio.orEmpty(),
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl,
        contributions = contributions
)

fun List<ContributorEntity>.toContributors(): List<Contributor> = map {
    it.toContributor()
}
