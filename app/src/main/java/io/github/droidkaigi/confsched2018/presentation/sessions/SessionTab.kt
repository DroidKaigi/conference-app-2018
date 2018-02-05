package io.github.droidkaigi.confsched2018.presentation.sessions

import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.model.Room as RoomModel

sealed class SessionTab {
    data class Room(val stuffs: List<RoomModel>) : SessionTab()
    data class Schedule(val stuffs: List<SessionSchedule>) : SessionTab()
}

enum class SessionTabMode {
    ROOM,
    SCHEDULE
}
