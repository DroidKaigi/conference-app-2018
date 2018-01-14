package io.github.droidkaigi.confsched2018.data.api.response

data class Contributor(
        var name: String,
        var bio: String = "",
        var avatarUrl: String,
        var htmlUrl: String,
        var contributions: Int
)
