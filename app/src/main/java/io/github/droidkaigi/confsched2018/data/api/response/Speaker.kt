package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Speaker(
        @SerializedName("firstName") val firstName: String? = null,
        @SerializedName("lastName") val lastName: String? = null,
        @SerializedName("profilePicture") val profilePicture: String? = null,
        @SerializedName("sessions") val sessions: List<Int?>? = null,
        @SerializedName("tagLine") val tagLine: String? = null,
        @SerializedName("isTopSpeaker") val isTopSpeaker: Boolean? = null,
        @SerializedName("bio") val bio: String? = null,
        @SerializedName("fullName") val fullName: String? = null,
        @SerializedName("links") val links: List<Link?>? = null,
        @SerializedName("id") val id: String? = null
)
