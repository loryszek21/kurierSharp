package com.example.kuriersharp.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import androidx.preference.PreferenceManager; // Ważne: użyj androidx.preference

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE_KEY = "language_preference"; // Klucz z preferences.xml

    // Wywoływane w Application.attachBaseContext() LUB Activity.attachBaseContext()
    public static Context onAttach(Context context) {
        String lang = getPersistedLanguage(context);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedLanguage(context);
    }

    public static Context setLocale(Context context, String languageCode) {
        persistLanguage(context, languageCode); // Zapisz wybrany język

        Locale locale;
        if (languageCode.equals("default") || languageCode.isEmpty()) { // "default" to nasza umowna wartość dla języka systemowego
            // Użyj konfiguracji systemowej, aby uzyskać domyślny język systemu
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                locale = Resources.getSystem().getConfiguration().locale;
            }
        } else {
            locale = new Locale(languageCode);
        }
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration()); // Skopiuj istniejącą konfigurację

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(locale);
            return context.createConfigurationContext(config);
        } else {
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context; // Na starszych API kontekst jest modyfikowany bezpośrednio
        }
    }

    private static String getPersistedLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE_KEY, "default"); // "default" jako domyślny
    }

    // Ta metoda jest wywoływana przez setLocale, więc nie musisz jej wywoływać ręcznie po zmianie preferencji,
    // jeśli PreferenceFragmentCompat sam zapisuje wartość. Jeśli nie, to tak.
    public static void persistLanguage(Context context, String languageCode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE_KEY, languageCode);
        editor.apply();
    }
}