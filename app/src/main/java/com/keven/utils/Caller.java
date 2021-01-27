package com.keven.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 方便多种线程调用的工具类
 * <p>
 * Created by jansonwu on 2017/8/11.
 */

public class Caller {
    private static final String TAG = "Caller";

    private static Handler sWorkHandler = null;

    static {
        HandlerThread mWorkThread = new HandlerThread("caller-thread");
        mWorkThread.start();

        Looper looper = mWorkThread.getLooper();
        if (looper != null) {
            sWorkHandler = new Handler(looper);
        }
    }

    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    public static Handler handlerOfUi() {
        return mUiHandler;
    }

    public static Handler handlerOfWorker() {
        return sWorkHandler;
    }

    public static void ui(Runnable r) {
        mUiHandler.post(r);
    }

    public static void ui(Runnable r, long delay) {
        mUiHandler.postDelayed(r, delay);
    }

    public static void delay(Runnable r, long delay) {
        if (delay <= 0) {
            r.run();
        } else {
            mUiHandler.postDelayed(r, delay);
        }
    }

    public static void async(Runnable r) {
        asyncImpl(r);
    }

    public static void async(final Runnable r, long delay) {
        if (sWorkHandler == null) {
            Log.e(TAG, "async: sWorkHandler is null");
            return;
        }
        sWorkHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                asyncImpl(r);
            }
        }, delay);
    }

    public static boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void uiToast(final Context context, final CharSequence text, final int duration) {
        ui(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, duration).show();
            }
        });
    }

    public static void uiToast(final Context context, final int textResId, final int duration) {
        ui(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, textResId, duration).show();
            }
        });
    }

    /**
     * 异步线程执行的实现
     *
     * @param runnable
     */
    private static void asyncImpl(final Runnable runnable) {
        // 这里的异步任务通常为较短，混合型的任务，直接使用AsyncTask
        AsyncTask.execute(runnable);
    }
}
