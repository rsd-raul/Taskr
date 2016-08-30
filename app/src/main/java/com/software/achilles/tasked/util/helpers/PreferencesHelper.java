package com.software.achilles.tasked.util.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.software.achilles.tasked.util.Constants;

public abstract class PreferencesHelper {

    // --------------------------- Values ----------------------------

    public abstract static class Keys{

        // App settings
        public static final String FIRST_TIME = "first_time";
        public static final String ORDER = "order";
    }
    public abstract static class Value{

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
    public static boolean getShaPrefBoolean(Context context, String key, boolean defaultValue, boolean system) {
        return getSharedPreferences(context, system).getBoolean(key, defaultValue);
    }


    // --------------------------- Setters ---------------------------

    public static void setShaPrefInteger(Context context, String key, Integer value, boolean system){
        getSharedPreferences_Editor(context, system).putInt(key, value).apply();
    }
    public static void setShaPrefBoolean(Context context, String key, Boolean value, boolean system){
        getSharedPreferences_Editor(context, system).putBoolean(key, value).apply();
    }

    // -------------------------- Auxiliary --------------------------

    private static SharedPreferences getSharedPreferences(Context context, boolean system){
        return context.getSharedPreferences(system ? Files.SYSTEM : Files.USER, Context.MODE_PRIVATE);
    }
    private static SharedPreferences.Editor getSharedPreferences_Editor(Context context, boolean system){
        return getSharedPreferences(context, system).edit();
    }
}