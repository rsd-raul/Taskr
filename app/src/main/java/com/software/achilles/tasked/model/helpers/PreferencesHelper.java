package com.software.achilles.tasked.model.helpers;

import android.content.Context;
import android.content.SharedPreferences;

// TODO - FINAL TESTING - Quitar y limpiar
@SuppressWarnings("unused")
public class PreferencesHelper {

    // --------------------------- Values ----------------------------

    public static class Keys{
        public static final String MAIN = "dash_or_glance";
        public static final String ADAPT_COLOR = "adaptColor";
        public static final String NOTIF_SOUND = "notificationTone";
        public static final String NOTIF_VIBRATION = "notificationVibration";
        public static final String NOTIF_LED = "notificationLed";
        public static final String SYNC_MODE = "syncMode";
        public static final String FIRST_DAY = "firstDay";
    }
    public static class Defaults{
        public static final String MAIN = "Dashboard";
        public static final boolean ADAPT_COLOR = true;
        public static final boolean NOTIF_SOUND = true;
        public static final boolean NOTIF_VIBRATION = true;
        public static final boolean NOTIF_LED = true;
        public static final String SYNC_MODE = "At app start";
        public static final String FIRST_DAY = "Monday";
    }

    public static class PreferenceFiles{
        // For preference between Dashboard and Glance
        public static final String SETTINGS = "settings";
        public static final String CONFIGURATIONS="configurations";
    }

    // --------------------------- Getters ---------------------------

    public static int getSharedPreferencesInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static String getSharedPreferencesString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static boolean getSharedPreferencesBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static float getSharedPreferencesFloat(Context context, String key, float defaultValue) {
        return getSharedPreferences(context).getFloat(key, defaultValue);
    }

    public static long getSharedPreferencesLong(Context context, String key, long defaultValue){
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    // --------------------------- Setters ---------------------------

    public static void setPreferenceInteger(Context context, String key, Integer value){
        getSharedPreferences_Editor(context).putInt(key, value).apply();
    }
    public static void setPreferenceString(Context context, String key, String value) {
        getSharedPreferences_Editor(context).putString(key, value).apply();
    }
    public static void setPreferenceBoolean(Context context, String key, Boolean value){
        getSharedPreferences_Editor(context).putBoolean(key, value).apply();
    }
    public static void setPreferenceFloat(Context context, String key, Float value){
        getSharedPreferences_Editor(context).putFloat(key, value).apply();
    }
    public static void setPreferenceLong(Context context, String key, Long value){
        getSharedPreferences_Editor(context).putLong(key, value).apply();
    }

    // -------------------------- Auxiliary --------------------------

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PreferenceFiles.SETTINGS, Context.MODE_PRIVATE);
    }
    private static SharedPreferences.Editor getSharedPreferences_Editor(Context context){
        return getSharedPreferences(context).edit();
    }
}