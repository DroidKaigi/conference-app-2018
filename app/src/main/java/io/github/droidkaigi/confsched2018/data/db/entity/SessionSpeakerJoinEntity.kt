package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(tableName = "session_speaker_join", primaryKeys = ["sessionId", "speakerId"],
        foreignKeys = [
            (ForeignKey(
                entity = SessionEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("sessionId"),
                    onDelete = CASCADE)),
            (ForeignKey(
                entity = SpeakerEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("speakerId"),
                onDelete = CASCADE))]
)
class SessionSpeakerJoinEntity(val sessionId: String, val speakerId: String)
