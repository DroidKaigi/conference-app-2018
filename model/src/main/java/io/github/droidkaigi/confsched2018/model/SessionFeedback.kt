package io.github.droidkaigi.confsched2018.model

data class SessionFeedback(
        val sessionId: String,
        val totalEvaluation: Int,
        val relevancy: Int,
        val asExpected: Int,
        val difficulty: Int,
        val knowledgeable: Int,
        val comment: String,
        val submitted: Boolean
) {
    val fillouted: Boolean
        get() = sessionId.isNotBlank()
                && totalEvaluation != 0
                && relevancy != 0
                && asExpected != 0
                && difficulty != 0
                && knowledgeable != 0
}
