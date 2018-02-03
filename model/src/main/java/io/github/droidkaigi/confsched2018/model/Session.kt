package io.github.droidkaigi.confsched2018.model

import java.util.Date

sealed class Session(
        open val id: String,
        open val dayNumber: Int,
        open val startTime: Date,
        open val endTime: Date
) {
    data class SpeechSession(
            override val id: String,
            override val dayNumber: Int,
            override val startTime: Date,
            override val endTime: Date,
            val title: String,
            val desc: String,
            val room: Room,
            val format: String,
            val language: String,
            val topic: Topic,
            val level: Level,
            val isFavorited: Boolean,
            val speakers: List<Speaker>,
            val feedback: SessionFeedback,
            val message: SessionMessage?
    ) : Session(id, dayNumber, startTime, endTime)

    data class SpecialSession(
            override val id: String,
            override val dayNumber: Int,
            override val startTime: Date,
            override val endTime: Date,
            val title: Int,
            val room: Room?
    ) : Session(id, dayNumber, startTime, endTime)

    val isFinished: Boolean
        get() = System.currentTimeMillis() > endTime.time

    val isOnGoing: Boolean
        get() = System.currentTimeMillis() in startTime.time..endTime.time
}
