package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class SessionWithSpeakers(
        @Embedded var session: SessionEntity? = null,
        @Relation(
                parentColumn = "id",
                entityColumn = "sessionId",
                projection = arrayOf("speakerId"),
                entity = SessionSpeakerJoinEntity::class)
        var speakerIdList: List<String> = emptyList())
