package cn.edu.pku.pkuhole.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import cn.edu.pku.pkuhole.R

class SettingsFragment2 : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}