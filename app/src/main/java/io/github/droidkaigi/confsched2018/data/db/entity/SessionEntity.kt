package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "session")
data class SessionEntity(
        @PrimaryKey var id: String,
        var title: String,
        var desc: String,
        var stime: LocalDateTime,
        var etime: LocalDateTime,
        var sessionFormat: String,
        var language: String,
        var topic: String,
        var level: String,
        @Embedded var room: RoomEntity
)
