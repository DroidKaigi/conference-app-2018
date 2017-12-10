package io.github.droidkaigi.confsched2018.data.entity

import android.arch.persistence.room.ColumnInfo


class TopicEntity(
//        var id: Int = 0
        @ColumnInfo(name = "topic_name")
        var name: String?,
        @ColumnInfo(name = "topic_other")
        var other: String?
)
