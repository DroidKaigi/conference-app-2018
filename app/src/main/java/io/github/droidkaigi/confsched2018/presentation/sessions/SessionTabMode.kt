package io.github.droidkaigi.confsched2018.presentation.sessions

sealed class SessionTabMode {
    object RoomTabMode: SessionTabMode()
    object ScheduleTabMode : SessionTabMode()
}
