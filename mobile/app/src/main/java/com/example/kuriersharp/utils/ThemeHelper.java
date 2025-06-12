package com.example.kuriersharp.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    public static final String LIGHT_MODE = "light";
    public static final String DARK_MODE = "dark";
    public static final String DEFAULT_MODE = "system"; // Klucz dla opcji systemowej

    public static void applyTheme(String themePref) {
        switch (themePref) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default: // SYSTEM_MODE lub jakikolwiek inny nieznany/domyślny
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}