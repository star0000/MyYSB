package com.mds.myysb.base;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Application instance;
    public static Context applicationContext;
    /** 当前上下文的引用 1*/
    private static Context context;

    public static Application getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        context = getApplicationContext();
    }


}
