package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "session_feedback")
data class SessionFeedbackEntity(
        @PrimaryKey @ColumnInfo(name = "session_id", index = true) var sessionId: String,
        @ColumnInfo(name = "total_evaluation") var totalEvaluation: Int,
        var relevancy: Int,
        var asExpected: Int,
        var difficulty: Int,
        var knowledgeable: Int,
        var comment: String,
        var submitted: Boolean
) {
    constructor() : this("", 0, 0, 0, 0, 0, "", false)
}
