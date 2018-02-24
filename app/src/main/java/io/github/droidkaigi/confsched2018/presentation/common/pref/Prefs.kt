package io.github.droidkaigi.confsched2018.presentation.common.pref

import com.chibatching.kotpref.KotprefModel
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.util.ext.bool

object Prefs : KotprefModel() {
    public override val kotprefName: String = "droidkaigi_prefs"

    var enableNotification: Boolean by booleanPref(
            default = true,
            key = R.string.pref_key_enable_notification
    )
}
