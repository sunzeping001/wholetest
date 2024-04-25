package com.example.pluginproject.application;

import android.app.Application;

import com.example.pluginproject.util.ContextUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.getInstance().setContext(this);
    }
}
