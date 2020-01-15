package com.keven.utils;

import android.os.Handler;
import android.os.HandlerThread;


public class MyHandlerThread extends HandlerThread {

    public MyHandlerThread() {
        super("MyHandlerThread");
        start();
    }

    public void postRun(Runnable runTask) {
        if (runTask != null) {
            new Handler(getLooper()).post(runTask);
        }
    }
}