package com.keven.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {
    public static Typeface DEFAULT = Typeface.DEFAULT;
    private static final String TAG = "FontUtils";
    private static FontUtils sSingleton;
    private Map<String, SoftReference<Typeface>> mCache = new HashMap();

    private FontUtils() {
    }

    public static FontUtils getInstance() {
        if (sSingleton == null) {
            synchronized (FontUtils.class) {
                if (sSingleton == null) {
                    sSingleton = new FontUtils();
                }
            }
        }
        return sSingleton;
    }

    public void replaceFontFromAsset(@NonNull View view, @NonNull String str) {
        replaceFont(view, createTypefaceFromAsset(view.getContext(), str));
    }

    public void replaceFontFromAsset(@NonNull View view, @NonNull String str, int i) {
        replaceFont(view, createTypefaceFromAsset(view.getContext(), str), i);
    }

    public void replaceFontFromFile(@NonNull View view, @NonNull String str) {
        replaceFont(view, createTypefaceFromFile(str));
    }

    public void replaceFontFromFile(@NonNull View view, @NonNull String str, int i) {
        replaceFont(view, createTypefaceFromFile(str), i);
    }

    private void replaceFont(@NonNull View view, @NonNull Typeface typeface) {
        if (view != null && typeface != null) {
            int i = 0;
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (textView.getTypeface() != null) {
                    i = textView.getTypeface().getStyle();
                }
                textView.setTypeface(typeface, i);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                while (i < viewGroup.getChildCount()) {
                    replaceFont(viewGroup.getChildAt(i), typeface);
                    i++;
                }
            }
        }
    }

    private void replaceFont(@NonNull View view, @NonNull Typeface typeface, int i) {
        if (view != null && typeface != null) {
            if (i < 0 || i > 3) {
                i = 0;
            }
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface, i);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                    replaceFont(viewGroup.getChildAt(i2), typeface, i);
                }
            }
        }
    }

    private Typeface createTypefaceFromAsset(Context context, String str) {
        Typeface typeface;
        SoftReference softReference = (SoftReference) this.mCache.get(str);
        if (softReference != null) {
            typeface = (Typeface) softReference.get();
            if (typeface != null) {
                return typeface;
            }
        }
        typeface = Typeface.createFromAsset(context.getAssets(), str);
        this.mCache.put(str, new SoftReference(typeface));
        return typeface;
    }

    private Typeface createTypefaceFromFile(String str) {
        Typeface typeface;
        SoftReference softReference = (SoftReference) this.mCache.get(str);
        if (softReference != null) {
            typeface = (Typeface) softReference.get();
            if (typeface != null) {
                return typeface;
            }
        }
        typeface = Typeface.createFromFile(str);
        this.mCache.put(str, new SoftReference(typeface));
        return typeface;
    }

    public void replaceSystemDefaultFontFromAsset(@NonNull Context context, @NonNull String str) {
        replaceSystemDefaultFont(createTypefaceFromAsset(context, str));
    }

    public void replaceSystemDefaultFontFromFile(@NonNull Context context, @NonNull String str) {
        replaceSystemDefaultFont(createTypefaceFromFile(str));
    }

    private void replaceSystemDefaultFont(@NonNull Typeface typeface) {
        modifyObjectField(null, "MONOSPACE", typeface);
    }

    private void modifyObjectField(Object obj, String str, Object obj2) {
        try {
            Field declaredField = Typeface.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
}