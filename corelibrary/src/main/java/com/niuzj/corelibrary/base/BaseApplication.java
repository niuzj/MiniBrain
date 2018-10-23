package com.niuzj.corelibrary.base;

import android.app.Application;

public class BaseApplication extends Application {
    public static BaseApplication sInstance;
    public static BaseApplication getInstance() {
        return sInstance;
    }
}
