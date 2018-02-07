package io.github.droidkaigi.confsched2018.presentation.common.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.sessions.SessionTabMode
import io.github.droidkaigi.confsched2018.util.ext.ScrollState
import io.github.droidkaigi.confsched2018.util.ext.integer

object PreviousSessionPrefs : KotprefModel() {
    public override val kotprefName: String = "previous_session_prefs"
    var previousSessionTabMode by enumValuePref(
            default = SessionTabMode.ROOM
    )
    var previousSessionTabIndex: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_tab_id)
    )
    private var previousSessionScrollPosition: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_scroll_position)
    )
    private var previousSessionScrollOffset: Int by intPref(
            default = context.integer(R.integer.pref_default_value_previous_session_scroll_offset)
    )

    fun initPreviousSessionPrefs() {
        PreviousSessionPrefs.previousSessionTabMode = SessionTabMode.ROOM
        PreviousSessionPrefs.previousSessionTabIndex = context.integer(
                integerRes = R.integer.pref_default_value_previous_session_tab_id)
        PreviousSessionPrefs.previousSessionScrollPosition = context.integer(
                integerRes = R.integer.pref_default_value_previous_session_scroll_position)
        PreviousSessionPrefs.previousSessionScrollOffset = context.integer(
                integerRes = R.integer.pref_default_value_previous_session_scroll_offset)
    }

    var scrollState: ScrollState
        get() = ScrollState(
                anchorPosition = previousSessionScrollPosition,
                anchorOffset = previousSessionScrollOffset)
        set(scrollState) {
            previousSessionScrollPosition = scrollState.anchorPosition
            previousSessionScrollOffset = scrollState.anchorOffset
        }
}
