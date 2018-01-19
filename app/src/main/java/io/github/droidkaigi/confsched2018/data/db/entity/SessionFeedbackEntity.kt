package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "session_feedback",
        foreignKeys = [(ForeignKey(
                entity = SessionEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("session_id"),
                onDelete = ForeignKey.CASCADE))])
data class SessionFeedbackEntity(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "session_id") val sessionId: String,
        @ColumnInfo(name = "total_evaluation") var totalEvaluation: Int,
        var relevancy: Int,
        var asExpected: Int,
        var difficulty: Int,
        var knowledgeable: Int,
        var comment: String,
        var submitted: Boolean
)
