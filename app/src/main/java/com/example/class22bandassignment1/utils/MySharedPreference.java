package com.example.class22bandassignment1.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    private static final String FILE_NAME = "FileGameData";
    //private static final String USERS = "usersData";

    private static MySharedPreference me;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String playerName;

    private MySharedPreference(Context ctx) {
        sharedPrefs = ctx.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }
    public static MySharedPreference getMe() {
        return me;
    }


    public static MySharedPreference init(Context ctx) {
        if (me == null) {
            me = new MySharedPreference(ctx);
        }
        return me;
    }

    public void putInt(String KEY, int value) {
        sharedPrefs.edit().putInt(KEY, value).apply();
    }
    public int getInt(String KEY, int defValue) {
        return sharedPrefs.getInt(KEY,0);
    }

    public String getString(String KEY, String defValue) {
        return sharedPrefs.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPrefs.edit().putString(KEY, value).apply();
    }
}
