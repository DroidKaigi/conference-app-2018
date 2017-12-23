package io.github.droidkaigi.confsched2018.data.api.response

data class Speaker(
        val firstName: String?,
        val lastName: String?,
        val profilePicture: String?,
        val sessions: List<Int?>?,
        val tagLine: String?,
        val isTopSpeaker: Boolean?,
        val bio: String?,
        val fullName: String?,
        val links: List<Link?>?,
        val id: String?
)
