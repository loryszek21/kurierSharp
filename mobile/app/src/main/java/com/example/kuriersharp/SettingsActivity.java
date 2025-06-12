package com.example.kuriersharp; // lub twój pakiet

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.kuriersharp.utils.LocaleHelper;
import com.example.kuriersharp.utils.ThemeHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // Ustaw język przed super.attachBaseContext, aby tytuł aktywności był poprawny
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Zastosuj motyw PRZED super.onCreate() i setContentView()
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String themePreferenceValue = prefs.getString("theme_preference", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePreferenceValue);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Utwórz ten layout poniżej

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment())
                    .commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ustawienia"); // Możesz to ustawić ze string.xml
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Prostsze niż onBackPressed(), jeśli nie masz skomplikowanej nawigacji wstecz
        return true;
    }
}