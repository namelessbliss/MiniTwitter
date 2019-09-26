package com.app.nb.minitwitter.common;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp INSTANCE;

    public static MyApp getInstance() {
        return INSTANCE;
    }

    public static Context getContext(){
        return INSTANCE;
    }

    /**
     * Singleton
     */
    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
    }
}
