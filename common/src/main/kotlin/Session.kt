package io.github.droidkaigi.confsched2018.model

data class Session(
        // FIXME: delete unused default values
        var id: String,
        var title: String,
        var desc: String = "",
        var startTime: Date = Date(),
        var endTime: Date = Date(),
        var durationMin: Int = 0,
        var type: String = "",
        var topic: Topic = Topic("", ""),
        var room: Room = Room(""),
        var lang: String = "",
        var format: String,
        var isFavorited: Boolean = false,
        var speakers: List<Speaker>
)
