package com.example.class22bandassignment1;

import android.app.Application;

import com.example.class22bandassignment1.utils.MySharedPreference;
import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

       MySharedPreference.init(this);
    }
}
