package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "session")
data class SessionEntity(
        @PrimaryKey var id: String = "",
        var title: String = "",
        var desc: String = "",
        var stime: Date = Date(),
        var etime: Date = Date(),
        var durationMin: Int = 0,
        var type: String = "",
        var lang: String = "",
        var sessionFormat: String = "",
        @Embedded var room: RoomEntity = RoomEntity()
)
