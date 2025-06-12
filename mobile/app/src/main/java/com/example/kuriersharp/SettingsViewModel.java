package com.example.kuriersharp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<String> selectedLanguage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> darkModeEnabled = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        loadSettings();
    }

    public LiveData<String> getSelectedLanguage() {
        return selectedLanguage;
    }

    public LiveData<Boolean> getDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setSelectedLanguage(String languageCode) {
        selectedLanguage.setValue(languageCode);
        saveLanguage(languageCode);
        // Ustaw język natychmiast
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    public void setDarkModeEnabled(boolean enabled) {
        darkModeEnabled.setValue(enabled);
        saveTheme(enabled);
        AppCompatDelegate.setDefaultNightMode(enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void loadSettings() {
        String savedLanguage = sharedPreferences.getString("language", Locale.getDefault().getLanguage());
        selectedLanguage.setValue(savedLanguage);

        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        darkModeEnabled.setValue(isDarkMode);
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

    // Dodaj pole context do ViewModel
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

}