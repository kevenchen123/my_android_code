package com.keven.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LogUtils {
    public static boolean DEBUG = true;
    public static File logFile;

    public static void enableDebug(Activity activity, boolean enable) {
        DEBUG = enable;

        PermissionUtil.requestPermission(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        String path = Environment.getExternalStorageDirectory().toString();
                        File dir = new File(path + File.separator + "MyApplication" + File.separator + "log");
                        if (!dir.exists()) {
                            dir.mkdirs();
                            Log.d("LogUtil", "Log Dir path=" + dir + "   " + dir.exists());
                        }
                        logFile = new File(dir, "log.txt");
                        if (!logFile.exists()) {
                            try {
                                Log.d("LogUtil", "create File :" +  logFile);
                                logFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                "请在设置中打开读写权限");
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
            addRecordToLog(tag, msg);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (DEBUG) {
            Log.d(tag, String.format(msg, args));
            addRecordToLog(tag, String.format(msg, args));
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
            addRecordToLog(tag, msg);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (DEBUG) {
            Log.i(tag, String.format(msg, args));
            addRecordToLog(tag, String.format(msg, args));
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
            addRecordToLog(tag, msg);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (DEBUG) {
            Log.e(tag, String.format(msg, args));
            addRecordToLog(tag, String.format(msg, args));
        }
    }

    public static void addRecordToLog(String TAG, String message) {
        if (logFile == null || !logFile.exists()) return;
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.write(Clock.REAL.millis() + "   " + TAG + "   " + message + "\r\n");
            buf.flush();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}