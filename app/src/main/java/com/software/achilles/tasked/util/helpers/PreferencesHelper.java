package com.software.achilles.tasked.util.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.software.achilles.tasked.util.Constants;

// REVIEW - Remove and Clean
@SuppressWarnings("unused")
public abstract class PreferencesHelper {

    // --------------------------- Values ----------------------------

    public abstract static class Keys{
        // User settings
        public static final String MAIN = "dash_or_glance";
        public static final String ADAPT_COLOR = "adaptColor";
        public static final String NOTIFICATION_SOUND = "notificationTone";
        public static final String NOTIFICATION_VIBRATION = "notificationVibration";
        public static final String NOTIFICATION_LED = "notificationLed";
        public static final String SYNC_MODE = "syncMode";
        public static final String FIRST_DAY = "firstDay";

        // App settings
        public static final String FIRST_TIME = "first_time";
        public static final String ORDER = "order";
    }
    public abstract static class Value{
        public static final String MAIN = "Dashboard";
        public static final boolean ADAPT_COLOR = true;
        public static final boolean NOTIFICATION_SOUND = true;
        public static final boolean NOTIFICATION_VIBRATION = true;
        public static final boolean NOTIFICATION_LED = true;
        public static final String SYNC_MODE = "At app start";
        public static final String FIRST_DAY = "Monday";

        // App settings
        public static final boolean FIRST_TIME = true;
        public static final int ORDER = Constants.NONE;
    }

    public static class Files{
        // For preference between Dashboard and Glance
        public static final String USER = "settings";
        public static final String SYSTEM = "configurations";
    }

    // --------------------------- Getters ---------------------------

    public static int getShaPrefInt(Context context, String key, int defaultValue, boolean system) {
        return getSharedPreferences(context, system).getInt(key, defaultValue);
    }

    public static String getShaPrefString(Context context, String key, String defaultValue, boolean system) {
        return getSharedPreferences(context, system).getString(key, defaultValue);
    }

    public static boolean getShaPrefBoolean(Context context, String key, boolean defaultValue, boolean system) {
        return getSharedPreferences(context, system).getBoolean(key, defaultValue);
    }

    public static float getShaPrefFloat(Context context, String key, float defaultValue, boolean system) {
        return getSharedPreferences(context, system).getFloat(key, defaultValue);
    }

    public static long getShaPrefLong(Context context, String key, long defaultValue, boolean system){
        return getSharedPreferences(context, system).getLong(key, defaultValue);
    }

    // --------------------------- Setters ---------------------------

    public static void setShaPrefInteger(Context context, String key, Integer value, boolean system){
        getSharedPreferences_Editor(context, system).putInt(key, value).apply();
    }
    public static void setShaPrefString(Context context, String key, String value, boolean system) {
        getSharedPreferences_Editor(context, system).putString(key, value).apply();
    }
    public static void setShaPrefBoolean(Context context, String key, Boolean value, boolean system){
        getSharedPreferences_Editor(context, system).putBoolean(key, value).apply();
    }
    public static void setShaPrefFloat(Context context, String key, Float value, boolean system){
        getSharedPreferences_Editor(context, system).putFloat(key, value).apply();
    }
    public static void setShaPrefLong(Context context, String key, Long value, boolean system){
        getSharedPreferences_Editor(context, system).putLong(key, value).apply();
    }

    // -------------------------- Auxiliary --------------------------

    private static SharedPreferences getSharedPreferences(Context context, boolean system){
        return context.getSharedPreferences(system ? Files.SYSTEM : Files.USER, Context.MODE_PRIVATE);
    }
    private static SharedPreferences.Editor getSharedPreferences_Editor(Context context, boolean system){
        return getSharedPreferences(context, system).edit();
    }
}