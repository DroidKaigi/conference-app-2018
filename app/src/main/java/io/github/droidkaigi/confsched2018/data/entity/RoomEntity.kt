package io.github.droidkaigi.confsched2018.data.entity

import android.arch.persistence.room.ColumnInfo

data class RoomEntity(
//        var id: Int = 0,
        @ColumnInfo(name = "room_name")
        var name: String?
)
