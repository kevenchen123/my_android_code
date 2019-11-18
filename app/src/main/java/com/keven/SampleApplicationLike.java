package com.keven;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.keven.hotfix.tinker.TinkerManager;
import com.keven.utils.CrashHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;

import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@DefaultLifeCycle(application = "com.keven.MyTinkerApplication" ,
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)//都是官方要求这么写的
public class SampleApplicationLike extends ApplicationLike {

    public static Application application = null;
    public static String accessToken = "";

    public SampleApplicationLike(Application application,
                            int tinkerFlags,
                            boolean tinkerLoadVerifyFlag,
                            long applicationStartElapsedTime,
                            long applicationStartMillisTime,
                            Intent tinkerResultIntent) {
        super(application,
                tinkerFlags,
                tinkerLoadVerifyFlag,
                applicationStartElapsedTime,
                applicationStartMillisTime,
                tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        application = getApplication();

        TinkerManager.initTinker(this);
        CrashHandler.getInstance().init(application);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}