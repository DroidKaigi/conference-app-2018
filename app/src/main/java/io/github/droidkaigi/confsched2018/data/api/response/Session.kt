package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Session(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("isServiceSession")
        val isServiceSession: Boolean? = null,
        @SerializedName("isPlenumSession")
        val isPlenumSession: Boolean? = null,
        @SerializedName("speakers")
        val speakers: List<String?>? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("startsAt")
        val startsAt: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("endsAt")
        val endsAt: String? = null,
        @SerializedName("roomId")
        val roomId: Int? = null,
        @SerializedName("categoryItems")
        val categoryItems: List<Int?>? = null
)
