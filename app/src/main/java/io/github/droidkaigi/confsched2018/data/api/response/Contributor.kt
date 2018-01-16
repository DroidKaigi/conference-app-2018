package io.github.droidkaigi.confsched2018.data.api.response

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Contributor(
        @Json(name = "login") val name: String,
        var bio: String?,
        @Json(name = "avatar_url") var avatarUrl: String,
        @Json(name = "html_url") var htmlUrl: String,
        var contributions: Int
)
