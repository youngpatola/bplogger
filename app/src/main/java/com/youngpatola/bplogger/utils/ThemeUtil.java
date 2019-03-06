package com.youngpatola.bplogger.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.youngpatola.bplogger.R;

public class ThemeUtil {
    public final static int LIGHT_THEME = 0;
    public final static int GRAY_THEME = 1;
    public final static int BLACK_THEME = 2;

    private static int selectedTheme = 0;

    public ThemeUtil() {
    }

    public static void assignTheme(int themeInt) {
        selectedTheme = themeInt;
    }

    public static void toggleTheme(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        switch(selectedTheme) {
            case 0 : editor.putInt("theme", LIGHT_THEME); break;
            case 1 : editor.putInt("theme", GRAY_THEME); break;
            case 2 : editor.putInt("theme", BLACK_THEME); break;
        }
        editor.apply();
    }

    public static void setupTheme(Activity activity) {
        Log.d("Add", "setupTheme: " + selectedTheme);
        switch (selectedTheme) {
            case LIGHT_THEME : activity.setTheme(R.style.AppTheme); break;
            case GRAY_THEME : activity.setTheme(R.style.AppTheme_Gray); break;
            case BLACK_THEME : activity.setTheme(R.style.AppTheme_Black); break;
        }
    }

    public static int getCurrentTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("theme", 0);
    }
}
