package com.keven.hotfix.tinker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keven.BuildConfig;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;


public class TinkerManager {
    private static final String TAG = "TinkerManager";
    private static boolean isInstalled = false;//是否已经初始化标志位
    private static ApplicationLike mApplicationLike;

    public static ApplicationLike getTinkerApplicationLike() {
        return mApplicationLike;
    }

    /**
     * 利用Tinker代理Application 获取应用全局的上下文
     * @return 全局的上下文
     */
    private static Context getApplicationContext() {
        if (mApplicationLike != null)
            return mApplicationLike.getApplication().getApplicationContext();
        return null;
    }

    /**
     * 完成Tinker初始化
     * @param applicationLike
     */
    public static void initTinker(ApplicationLike applicationLike) {
        mApplicationLike = applicationLike;
        UpgradePatchRetry.getInstance(mApplicationLike.getApplication()).setRetryEnable(true);
        installTinker(mApplicationLike);
        Tinker tinker = Tinker.with(mApplicationLike.getApplication());
    }

    /**
     * all use default class, simply Tinker install method
     */
    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;
    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new SampleLoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new SamplePatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        PatchListener patchListener = new SamplePatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(appLike,
                loadReporter, patchReporter, patchListener,
                SampleResultService.class, upgradePatchProcessor);

        isInstalled = true;
    }

    /**
     * 完成patch文件的加载
     */
    public static void loadPatch() {
        if (Tinker.isTinkerInstalled()) {//是否已经安装过
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
        }
    }

    public static void clearPatch() {
        if (Tinker.isTinkerInstalled()) {//是否已经安装过
            Tinker.with(getApplicationContext()).cleanPatch();
        }
    }

    public static String MESSAGE       = BuildConfig.MESSAGE;
    public static String TINKER_ID     = BuildConfig.TINKER_ID;
    public static String PLATFORM      = BuildConfig.PLATFORM;

    public static boolean showInfo(Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));
        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", "I won't change with tinker patch!"));

        final TextView v = new TextView(context);
        v.setText(sb);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setTextColor(0xFF000000);
        v.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        v.setPadding(padding, padding, padding, padding);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(v);
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}