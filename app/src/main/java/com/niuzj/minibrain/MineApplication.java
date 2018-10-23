package com.niuzj.minibrain;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.niuzj.corelibrary.base.BaseApplication;
import com.niuzj.minibrain.bean.MyObjectBox;

import io.objectbox.BoxStore;

public class MineApplication extends BaseApplication {

    private BoxStore mBoxStore;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBoxStore = MyObjectBox.builder().androidContext(this).build();
        sInstance = this;
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public static MineApplication getInstance() {
        return (MineApplication) sInstance;
    }


}
