package com.keven.utils;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import java.lang.reflect.Method;

public class KeyEventUtils {
    private static long mDownTime;

    public static void sendEvent(int keycode) {
        sendEvent(KeyEvent.ACTION_DOWN, keycode, 0, mDownTime = SystemClock.uptimeMillis());
        sendEvent(KeyEvent.ACTION_UP, keycode, 0, SystemClock.uptimeMillis());
    }

    private static void sendEvent(int action, int keycode, int flags, long when) {
        final int repeatCount = (flags & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent ev = new KeyEvent(mDownTime, when, action, keycode, repeatCount,
                0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                flags | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_KEEP_TOUCH_MODE | KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_ANY);

        try {
            InputManager im = (InputManager) InputManager.class.getMethod("getInstance", null).invoke(null, null);
            if (im != null) {
                Method instance = im.getClass().getMethod("injectInputEvent", InputEvent.class, int.class);
                instance.invoke(im, ev, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}