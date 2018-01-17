package io.github.droidkaigi.confsched2018.model

data class SessionFeedback(
        val sessionId: String,
        val sessionTitle: String,
        val totalEvaluation: Int,
        val relevancy: Int,
        val asExpected: Int,
        val difficulty: Int,
        val knowledgeable: Int,
        val comment: String,
        var submitted: Boolean
)
