package io.github.droidkaigi.confsched2018.model

import java.util.*

data class Session(
        var id: Int,
        var title: String,
        var desc: String,
        var speaker: Speaker,
        var stime: Date,
        var etime: Date,
        var durationMin: Int,
        var type: String,
        var topic: Topic,
        var room: Room,
        var lang: String,
        var isFavorited: Boolean
)
