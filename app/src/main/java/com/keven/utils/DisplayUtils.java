package com.keven.utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DisplayUtils {

    //分辨率转换
    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static float px2sp(Context context, Float px) {
        if (context == null) {
            return px.intValue();
        }
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static float sp2px(Context context, float sp) {
        if (context == null) {
            return sp;
        }
        Resources r = context.getResources();
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
        return size;
    }

    //屏幕尺寸
    public static int[] getScreenHeightPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        int[] size = new int[] {dm.widthPixels, dm.heightPixels};
        return size;
    }

    //是否是pad
    public static boolean isPad(Activity activity) {
        TelephonyManager telephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    //屏幕保亮
    public void keepScreenAwake(Context app, String name) {
        PowerManager power = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
        power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, name)
                .acquire(10 * 60 * 1000L /*10 minutes*/);
    }

    //屏解锁
    public void unlockScreen(final Activity activity) {
        Runnable wakeUpDevice = new Runnable() {
            @Override
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    public void unlockScreen(Context app, String name) {
        KeyguardManager keyguard = (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(name).disableKeyguard();
    }

    //软键盘
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    //软键盘高度
    public static void softKeyboardSate(Context context) {
        try {
            InputMethodManager mInputmm = (InputMethodManager)context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Method mMethod = mInputmm.getClass().getMethod("getInputMethodWindowVisibleHeight", new  Class[ 0 ]);
            mMethod.setAccessible(true);
            int height = (Integer) mMethod.invoke(mInputmm, new  Object[]{});
            Log.i("TAG","height=="+height);

            if (height > 0) {
                // 软件盘弹出  else 就是收回
                new Thread(){
                    @Override
                    public void run() {
                        Instrumentation instrumentation = new Instrumentation();
                        instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }.start();
            }
        } catch (Exception exception) {
            Log.e("TAG", "softKeyboardSate error!");
            exception.printStackTrace();
        }
    }


    //导航栏高度
    public static int getNavBarSize(Context context) {
        Point size = new Point();
        Point realSize = new Point();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        display.getRealSize(realSize);
        return realSize.y - size.y;
    }

    // 获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    // 是否开启了虚拟按键
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


    //状态栏
    public static void setStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = new View(window.getContext());
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarView);
        }
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    // https://github.com/luckyshane/TintBar
    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    // 开关状态栏是否可以下拉
    public static void setStatusbarExpand(Context context, boolean expandble) {
        int DISABLE_EXPAND = 0x00010000;//4.2以上的整形标识
        int DISABLE_EXPAND_LOW = 0x00000001;//4.2以下的整形标识
        int EXPAND = 0x00000000;

        Object service = context.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            if (expandble) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    expand.invoke(service, DISABLE_EXPAND);
                } else {
                    expand.invoke(service, DISABLE_EXPAND_LOW);
                }
            }else {
                expand.invoke(service, EXPAND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void systemUIVisible(View view) {
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                boolean systemUiVisible = (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
            }
        });
    }


    //设置Editetxt Hint 文字大小
    public static void setHint(EditText editText, String hint, int fontSize) {
        SpannableString ss = new SpannableString(hint);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(fontSize, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    //设置TextView光标
    public static void setTextCursor(EditText et, Drawable drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(et, drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 屏幕是否暗
    public static class ScreenState {
        public interface IOnScreenOff {
            void onScreenOff();
        }

        public ScreenState(final Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent in) {
                    String action = in == null ? "" : in.getAction();
                    Log.i("ScreenState", "ScreenReceiver action = " + action);
                    if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                    context.unregisterReceiver(this);
                }
            }, filter);
        }
    }


    // 全屏
    public static void fullScreen(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        context.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE  //设置状态栏和导航栏中的图标变小，变模糊或者弱化其效果。
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE //稳定布局，主要是在全屏和非全屏切换时，布局不要有大的变化。
                        | View.SYSTEM_UI_FLAG_IMMERSIVE //使状态栏和导航栏真正的进入沉浸模式,即全屏模式，如果没有设置这个标志，设置全屏时，我们点击屏幕的任意位置，就会恢复为正常模式。
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY //它在全屏模式下，用户上下拉状态栏或者导航栏时，这些系统栏只是以半透明的状态显示出来，并且在一定时间后会自动消息。
                        | View.SYSTEM_UI_FLAG_FULLSCREEN  //隐藏状态栏，点击屏幕区域不会出现，需要从状态栏位置下拉才会出现。(如果有EditText获取焦点就不起作用)
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //将布局内容拓展到状态的后面。
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  //隐藏导航栏，点击屏幕任意区域，导航栏将重新出现，并且不会自动消失。
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //将布局内容拓展到导航栏的后面。
        );
    }
}