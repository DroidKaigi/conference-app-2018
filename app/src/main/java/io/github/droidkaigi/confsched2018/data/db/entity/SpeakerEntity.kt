package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "speaker")
class SpeakerEntity(
        @PrimaryKey var id: String,
        @ColumnInfo(name = "speaker_name")
        var name: String,
        @ColumnInfo(name = "speaker_image_url")
        var imageUrl: String = "",
        @ColumnInfo(name = "speaker_twitter_name")
        var twitterName: String = "",
        @ColumnInfo(name = "speaker_github_name")
        var githubName: String = ""
)
