package io.github.droidkaigi.confsched2018.data.api.response

import com.google.gson.annotations.SerializedName

data class Response(
        @SerializedName("sessions")
        val sessions: List<Session>? = null,
        @SerializedName("rooms")
        val rooms: List<Room>? = null,
        @SerializedName("speakers")
        val speakers: List<Speaker>? = null,
        @SerializedName("categories")
        val categories: List<Category>? = null
)

