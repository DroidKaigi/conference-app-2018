package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "session")
data class SessionEntity(
        @PrimaryKey var id: String = "",
        var title: String = "",
        var desc: String = "",
        var stime: LocalDateTime = LocalDateTime.of(1, 1, 1, 1, 1),
        var etime: LocalDateTime = LocalDateTime.of(1, 1, 1, 1, 1),
        var durationMin: Int = 0,
        var type: String = "",
        var lang: String = "",
        var sessionFormat: String = "",
        @Embedded var room: RoomEntity = RoomEntity()
)
