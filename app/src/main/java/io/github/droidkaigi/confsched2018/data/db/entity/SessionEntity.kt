package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(tableName = "session")
data class SessionEntity(
        @PrimaryKey var id: String,
        var title: String,
        var desc: String,
        var stime: Instant,
        var etime: Instant,
        var sessionFormat: String,
        var language: String,
        @Embedded var level: LevelEntity,
        @Embedded var topic: TopicEntity,
        @Embedded var room: RoomEntity,
        @Embedded val message: MessageEntity?
)
