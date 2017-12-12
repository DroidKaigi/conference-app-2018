package io.github.droidkaigi.confsched2018.model

import java.util.*

data class Session(
        // FIXME: delete unused default values
        var id: String,
        var title: String,
        var desc: String = "",
        var stime: Date = Date(0),
        var etime: Date = Date(0),
        var durationMin: Int = 0,
        var type: String = "",
        var topic: Topic = Topic("", ""),
        var room: Room = Room(""),
        var lang: String = "",
        var format: String,
        var isFavorited: Boolean = false,
        var speakers: List<Speaker>
)
