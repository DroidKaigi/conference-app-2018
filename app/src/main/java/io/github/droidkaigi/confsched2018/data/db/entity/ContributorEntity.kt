package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "contributor")
data class ContributorEntity(
        @PrimaryKey var name: String,
        var bio: String? = "",
        var avatarUrl: String,
        var htmlUrl: String,
        var contributions: Int
)
