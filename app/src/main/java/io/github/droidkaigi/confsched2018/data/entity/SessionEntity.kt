package io.github.droidkaigi.confsched2018.data.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "Session")
data class SessionEntity(
        @PrimaryKey var id: Int?,
        var title: String?,
        var desc: String?,
        @Embedded var speaker: SpeakerEntity?,
        var stime: Date?,
        var etime: Date?,
        var durationMin: Int?,
        var type: String?,
        @Embedded var topic: TopicEntity?,
        @Embedded var room: RoomEntity?,
        var lang: String?
)
