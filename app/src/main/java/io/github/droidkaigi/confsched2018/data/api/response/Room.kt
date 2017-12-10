package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Room(
        @SerializedName("name") val name: String? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("sort") val sort: Int? = null
)
