package io.github.droidkaigi.confsched2018.presentation.common.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionTabMode
import io.github.droidkaigi.confsched2018.util.ext.bool
import io.github.droidkaigi.confsched2018.util.ext.integer

object Prefs : KotprefModel() {
    public override val kotprefName: String = "droidkaigi_prefs"
    var enableLocalTime: Boolean by booleanPref(
            default = context.bool(R.bool.pref_default_value_enable_local_time),
            key = R.string.pref_key_enable_local_time
    )
    var enableNotification: Boolean by booleanPref(
            default = context.bool(R.bool.pref_default_value_enable_notification),
            key = R.string.pref_key_enable_notification
    )
    var enableHideBottomNavigationBar: Boolean by booleanPref(
            default = context.bool(R.bool.pref_default_value_enable_hide_bottom_navigation),
            key = R.string.pref_key_enable_hide_bottom_navigation
    )
    var enableReopenPreviousRoomSessions: Boolean by booleanPref(
            default = context.bool(R.bool.pref_default_value_enable_reopen_previous_room_sessions),
            key = R.string.pref_key_enable_reopen_previous_room_sessions
    )
    var previousSessionTabMode by enumValuePref(SessionTabMode.ROOM)
    var previousSessionTabId: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_tab_id)
    )
    var previousSessionScrollPosition: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_scroll_position)
    )
    var previousSessionScrollOffset: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_scroll_offset)
    )

    fun initPreviousSessionPrefs() {
        Prefs.previousSessionTabMode = SessionTabMode.ROOM
        Prefs.previousSessionTabId = Prefs.context.integer(
                integerRes = R.integer.pref_default_value_previous_session_tab_id)
        Prefs.previousSessionScrollPosition = Prefs.context.integer(
                integerRes = R.integer.pref_default_value_previous_session_scroll_position)
        Prefs.previousSessionScrollOffset = Prefs.context.integer(
                integerRes = R.integer.pref_default_value_previous_session_scroll_offset)
    }
}
