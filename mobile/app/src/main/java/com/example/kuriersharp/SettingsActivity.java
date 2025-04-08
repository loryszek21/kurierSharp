package com.example.kuriersharp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.kuriersharp.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Switch themeSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        languageSpinner = findViewById(R.id.languageSpinner);
        themeSwitch = findViewById(R.id.themeSwitch);
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Konfiguracja Spinnera języków
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                this, R.array.language_options, android.R.layout.simple_spinner_dropdown_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        // Ustawienie domyślnego języka i motywu (z SharedPreferences)
        loadSettings();

        // Obsługa zmiany języka
        languageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String[] languageCodes = getResources().getStringArray(R.array.language_codes);
                String selectedLanguageCode = languageCodes[position];
                setLocale(selectedLanguageCode);
                saveLanguage(selectedLanguageCode); // Zapisz język w SharedPreferences

                // **Dodaj ten kod do ponownego uruchomienia MainActivity:**
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Opcjonalne flagi
                startActivity(intent);
//                finish(); // Zamknij SettingsActivity
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Nic nie rób
            }
        });

        // Obsługa zmiany motywu
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveTheme(true); // Zapisz motyw w SharedPreferences
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveTheme(false); // Zapisz motyw w SharedPreferences
            }
            // Ponownie utwórz Activity, aby zmiany motywu były widoczne od razu
            recreate();
        });
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void loadSettings() {
        // Język
        String savedLanguage = sharedPreferences.getString("language", "en"); // Domyślny język: polski
        String[] languageCodes = getResources().getStringArray(R.array.language_codes);
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(savedLanguage)) {
                languageSpinner.setSelection(i);
                setLocale(savedLanguage); // Ustaw język przy starcie aplikacji
                break;
            }
        }

        // Motyw
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false); // Domyślny motyw: jasny
        themeSwitch.setChecked(isDarkMode);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void saveLanguage(String languageCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    private void saveTheme(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkMode", isDarkMode);
        editor.apply();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Ustaw język przed utworzeniem kontekstu Activity, aby język był poprawny od początku
        String savedLanguage = newBase.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .getString("language", "en");
        Locale locale = new Locale(savedLanguage);
        Context context = newBase.createConfigurationContext(new Configuration());
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        newBase = context.createConfigurationContext(config);
        super.attachBaseContext(newBase);
    }
}