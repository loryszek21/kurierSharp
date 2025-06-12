package com.example.kuriersharp; // lub twój pakiet

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.kuriersharp.utils.LocaleHelper;
import com.example.kuriersharp.utils.ThemeHelper;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Ustawianie początkowych wartości summary dla ListPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        updateListPreferenceSummary(findPreference("theme_preference"), sharedPreferences.getString("theme_preference", ThemeHelper.DEFAULT_MODE));
        updateListPreferenceSummary(findPreference("language_preference"), sharedPreferences.getString("language_preference", "default"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            String value = sharedPreferences.getString(key, "");
            updateListPreferenceSummary(listPreference, value); // Aktualizuj summary

            if (key.equals("theme_preference")) {
                ThemeHelper.applyTheme(value);
                requireActivity().recreate(); // Zastosuj zmiany motywu natychmiast
            } else if (key.equals("language_preference")) {
                LocaleHelper.setLocale(requireActivity(), value); // setLocale zapisze preferencję
                requireActivity().recreate(); // Zastosuj zmiany języka natychmiast
            }
        }
    }

    private void updateListPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
    }
}