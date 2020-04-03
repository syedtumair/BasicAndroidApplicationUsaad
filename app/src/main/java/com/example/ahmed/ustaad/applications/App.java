package com.example.ahmed.ustaad.applications;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }

    public static Context getAppContext() {
        return App.applicationContext;
    }


}
