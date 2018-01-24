package io.github.droidkaigi.confsched2018.model

data class SessionFeedback(
        val sessionId: String,
        var sessionTitle: String,
        var totalEvaluation: Int,
        var relevancy: Int,
        var asExpected: Int,
        var difficulty: Int,
        var knowledgeable: Int,
        var comment: String,
        var submitted: Boolean
)
