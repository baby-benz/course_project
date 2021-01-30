package com.example.course_project;

import android.app.Application;
import android.content.Context;

public class Main extends Application {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}