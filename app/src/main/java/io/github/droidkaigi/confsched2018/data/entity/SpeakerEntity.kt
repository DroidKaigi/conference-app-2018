package io.github.droidkaigi.confsched2018.data.entity

import android.arch.persistence.room.ColumnInfo

class SpeakerEntity(
//        var id: Int = 0
        @ColumnInfo(name = "speaker_name")
        var name: String?,
        @ColumnInfo(name = "speaker_image_url")
        var imageUrl: String?,
        @ColumnInfo(name = "speaker_twitter_name")
        var twitterName: String?,
        @ColumnInfo(name = "speaker_github_name")
        var githubName: String?
)
