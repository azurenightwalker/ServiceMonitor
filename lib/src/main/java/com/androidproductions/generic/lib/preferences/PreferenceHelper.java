package com.androidproductions.generic.lib.preferences;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferenceHelper {
    private static PreferenceHelper ourInstance;
    private final Context mContext;

    public static PreferenceHelper getInstance() {
        return ourInstance;
    }

    public static void init(Context context)
    {
        ourInstance = new PreferenceHelper(context);
    }

    private PreferenceHelper(Context context) {
        mContext = context;
    }

    public boolean getBoolean(String property) {
        return getBoolean(property,true);
    }

    public boolean getBoolean(String property, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(property,defaultValue);
    }

    public void setBoolean(String property, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                .putBoolean(property, value).commit();
    }

    public int getInt(String property) {
        return getInt(property,0);
    }

    public int getInt(String property, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getInt(property,defaultValue);
    }

    public void setInt(String property, int value) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                .putInt(property, value).commit();
    }
}
