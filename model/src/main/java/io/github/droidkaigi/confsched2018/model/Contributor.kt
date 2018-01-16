package io.github.droidkaigi.confsched2018.model

data class Contributor(
        val name: String,
        var bio: String?,
        var avatarUrl: String,
        var htmlUrl: String,
        var contributions: Int
)

