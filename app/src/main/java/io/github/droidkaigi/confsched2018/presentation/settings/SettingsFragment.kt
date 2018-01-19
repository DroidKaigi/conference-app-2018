package io.github.droidkaigi.confsched2018.presentation.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.presentation.common.pref.Prefs

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Prefs.kotprefName
        addPreferencesFromResource(R.xml.preferences)
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}
