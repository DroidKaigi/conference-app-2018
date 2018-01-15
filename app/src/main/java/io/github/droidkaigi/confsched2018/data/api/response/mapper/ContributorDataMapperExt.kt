package io.github.droidkaigi.confsched2018.data.api.response.mapper

import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity

fun List<Contributor>?.toEntities(): List<ContributorEntity> =
        this!!.map {
            ContributorEntity(
                    name = it.name,
                    bio = it.bio,
                    avatarUrl = it.avatarUrl,
                    htmlUrl = it.htmlUrl,
                    contributions = it.contributions
            )
        }
