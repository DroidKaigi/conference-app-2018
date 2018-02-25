package io.github.droidkaigi.confsched2018.presentation.action

import io.github.droidkaigi.confsched2018.model.Session

data class SessionChangedAction(val sessions: List<Session>)
