package io.github.droidkaigi.confsched2018.model

data class Session(
        var id: String,
        var title: String,
        var desc: String,
        var startTime: Date,
        var endTime: Date,
        var room: Room = Room(""),
        var format: String,
        var sessionFormat: String,
        var language: String,
        var topic: String,
        var level: String,
        var isFavorited: Boolean,
        var speakers: List<Speaker>
)
