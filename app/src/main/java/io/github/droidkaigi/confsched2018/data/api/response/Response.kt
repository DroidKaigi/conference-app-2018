package io.github.droidkaigi.confsched2018.data.api.response


data class Response(
        val sessions: List<Session>?,
        val rooms: List<Room>?,
        val speakers: List<Speaker>?,
        val categories: List<Category>?
)

