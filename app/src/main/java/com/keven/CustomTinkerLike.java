package com.keven;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.keven.hotfix.TinkerManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;

import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@DefaultLifeCycle(application = "com.keven.MyTinkerApplication" ,
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)//都是官方要求这么写的
public class CustomTinkerLike extends ApplicationLike {

    public static String accessToken = "";

    public CustomTinkerLike(Application application,
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
        TinkerManager.installedTinker(this);

        //其他初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}