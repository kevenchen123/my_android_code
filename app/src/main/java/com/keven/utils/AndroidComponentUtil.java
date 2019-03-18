package com.keven.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.keven.utils.executor.BackgroundExecutor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AndroidComponentUtil {

    public static void toggleComponent(Context context, Class componentClass, boolean enable) {
        ComponentName componentName = new ComponentName(context, componentClass);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void restartSingleTaskActivity(Context context) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    public static void toActivityCall(Context context, String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // get Main action and special Category activity info in this app
    public static Map<String, Intent> filterActivity(Context context, String CATEGORY_EXAMPLE) {
        Map<String, Intent> data = new HashMap<>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.setPackage(context.getApplicationContext().getPackageName());
        mainIntent.addCategory(CATEGORY_EXAMPLE);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        if (list == null) {
            return data;
        }

        for (ResolveInfo info : list) {
            CharSequence labelSep = info.loadLabel(pm);
            String label = labelSep != null ? labelSep.toString() : info.activityInfo.name;
            Log.d("TAG", ">>>" + label);

            String[] labelPath = label.split("/");
            String nextLabel = labelPath[0];
            if (labelPath.length == 1) {
                String nameLabel = label;
                Intent intent = new Intent();
                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                data.put(nameLabel, intent);
                Log.d("TAG", ">>>" + intent);
            }
        }
        return data;
    }

    /**
     * Reload the 6 buttons with recent activities
     */
    public static ArrayList<RecentTag> getRecentTask(Context context, int NUM_BUTTONS) {
        final ArrayList<RecentTag> recentTags = new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //拿到最近使用的应用的信息列表
        final List<ActivityManager.RecentTaskInfo> recentTasks =
                am.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        //自制一个home activity info，用来区分
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);

        // Performance note:  Our android performance guide says to prefer Iterator when
        // using a List class, but because we know that getRecentTasks() always returns
        // an ArrayList<>, we'll use a simple index instead.
        int index = 0;
        int numTasks = recentTasks.size();//开始初始化每个任务的信息

        for (int i = 0; i < numTasks && (index < NUM_BUTTONS); ++i) {
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent); //复制一个任务的原始Intent
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            // Skip the current home activity.
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent().getPackageName())
                        && homeInfo.name.equals(intent.getComponent().getClassName())) {
                    continue;
                }
            }

            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final String title = activityInfo.loadLabel(pm).toString();
                Drawable icon = activityInfo.loadIcon(pm);

                if (title != null && title.length() > 0 && icon != null) {
                    RecentTag tag = new RecentTag();//new一个Tag，保存这个任务的RecentTaskInfo和Intent
                    tag.info = info;
                    tag.intent = intent;
                    tag.title = title;
                    tag.icon = icon;
                    recentTags.add(tag);
                    Log.d("TAG", ">>>"+tag);
                    ++index;
                }
            }
        }
        return recentTags;
    }

    public static class RecentTag {
        public ActivityManager.RecentTaskInfo info;
        public Intent intent;
        public Drawable icon;
        public String title;

        @Override
        public String toString() {
            return "RecentTag{" +
                    "info=" + info.id +" # " + info +
                    ", intent=" + intent +
                    ", icon=" + icon +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * @return the top running task (can be {@code null}).
     */
    public static ActivityManager.RunningTaskInfo getRunningTask(Context context) {
        try {
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks.isEmpty()) {
                return null;
            }
            Log.d("TAG", ">>>"+tasks.get(0).topActivity.getClassName());
            return tasks.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Removes a task by id.
     */
    public static void removeTask(Context context, int taskId) {
        BackgroundExecutor.get().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    Method instance = am.getClass().getMethod("removeTask", int.class);
                    instance.invoke(am, taskId);
                } catch (Exception e) {
                    Log.w("TAG", "Failed to remove task=" + taskId, e);
                }
            }
        });
    }


    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}