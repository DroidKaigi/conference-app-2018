package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo

class TopicEntity(
        @ColumnInfo(name = "topic_name")
        var name: String,
        @ColumnInfo(name = "topic_other")
        var other: String
)
