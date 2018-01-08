package io.github.droidkaigi.confsched2018.model

sealed class Session(
        open var id: String,
        open var title: String,
        open var dayNumber: Int,
        open var startTime: Date,
        open var endTime: Date
) {
    data class SpeechSession(
            override var id: String,
            override var title: String,
            var desc: String,
            override var dayNumber: Int,
            override var startTime: Date,
            override var endTime: Date,
            var room: Room,
            var format: String,
            var language: String,
            var topic: Topic,
            var level: Level,
            var isFavorited: Boolean,
            var speakers: List<Speaker>
    ):Session(id, title, dayNumber, startTime, endTime)

    data class SpecialSession(
            override var id: String,
            override var title: String,
            override var dayNumber: Int,
            override var startTime: Date,
            override var endTime: Date,
            var room: Room?
    ):Session(id, title, dayNumber, startTime, endTime)

}
