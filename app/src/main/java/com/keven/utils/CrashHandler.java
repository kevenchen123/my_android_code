package com.keven.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Map<String, String> infos = new HashMap();
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    private void sendCrashLog2PM(java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.miren.base.CrashHandler.sendCrashLog2PM(java.lang.String):void");
    }

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (!handleException(th)) {
            UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultHandler;
            if (uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(thread, th);
                return;
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        }
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    private boolean handleException(final Throwable th) {
        if (th == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        new Thread() {
            public void run() {
                Looper.prepare();
                Context access$000 = CrashHandler.this.mContext;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("很抱歉,程序出现异常,即将退出.");
                stringBuilder.append(th.toString());
                Toast.makeText(access$000, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        saveCatchInfo2File(th);
        return true;
    }

    public void collectDeviceInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), GET_ACTIVITIES);
            if (packageInfo != null) {
                String obj = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(packageInfo.versionCode);
                stringBuilder.append("");
                String stringBuilder2 = stringBuilder.toString();
                this.infos.put("versionName", obj);
                this.infos.put("versionCode", stringBuilder2);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                String str = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(field.getName());
                stringBuilder3.append(" : ");
                stringBuilder3.append(field.get(null));
                Log.d(str, stringBuilder3.toString());
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCatchInfo2File(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Entry entry : this.infos.entrySet()) {
            String str = (String) entry.getKey();
            String str2 = (String) entry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("=");
            stringBuilder.append(str2);
            stringBuilder.append("\n");
            stringBuffer.append(stringBuilder.toString());
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (th = th.getCause(); th != null; th = th.getCause()) {
            th.printStackTrace(printWriter);
        }
        printWriter.close();
        stringBuffer.append(stringWriter.toString());
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String format = this.formatter.format(new Date());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("crash-");
            stringBuilder2.append(format);
            stringBuilder2.append("-");
            stringBuilder2.append(currentTimeMillis);
            stringBuilder2.append(".log");
            format = stringBuilder2.toString();
            if (Environment.getExternalStorageState().equals("mounted")) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(Environment.getExternalStorageDirectory().getPath());
                stringBuilder3.append("/crash/");
                String stringBuilder4 = stringBuilder3.toString();
                File file = new File(stringBuilder4);
                if (!file.exists()) {
                    file.mkdirs();
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(stringBuilder4);
                stringBuilder2.append(format);
                FileOutputStream fileOutputStream = new FileOutputStream(stringBuilder2.toString());
                fileOutputStream.write(stringBuffer.toString().getBytes());
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(stringBuilder4);
                stringBuilder5.append(format);
                sendCrashLog2PM(stringBuilder5.toString());
                fileOutputStream.close();
            }
            return format;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }
}
