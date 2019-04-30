package com.keven;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class App extends Application {

    private static Context mContext;
    public static String accessToken = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getAppContext() {
        return mContext;
    }
}
