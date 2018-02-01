package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo

data class MessageEntity(
        @ColumnInfo(name = "message_ja")
        var ja: String,
        @ColumnInfo(name = "message_en")
        var en: String
)
