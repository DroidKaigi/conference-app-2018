package io.github.droidkaigi.confsched2018.presentation.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import io.github.droidkaigi.confsched2018.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}
