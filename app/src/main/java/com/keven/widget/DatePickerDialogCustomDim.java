package com.keven.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

public class DatePickerDialogCustomDim extends DatePickerDialog {

    private final long animDuration = 100;
    private float dimAmount = 0.7f;

    private Drawable dimDrawable;
    private ViewGroup root;

    private OnDismissListener outsideDismissListener;

    private final OnDismissListener dismissListener = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(dimDrawable,
                    PropertyValuesHolder.ofInt("alpha", (int) (255 * dimAmount), 0));
            animator.setTarget(dimDrawable);
            animator.setDuration(animDuration);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ViewGroupOverlay overlay = root.getOverlay();
                    overlay.remove(dimDrawable);
                }
            });
            animator.start();
            if (outsideDismissListener != null)
                outsideDismissListener.onDismiss(dialog);
        }
    };


    @TargetApi(Build.VERSION_CODES.N)
    public DatePickerDialogCustomDim(@NonNull Context context) {
        this(context, 0);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public DatePickerDialogCustomDim(@NonNull Context context, @StyleRes int themeResId) {
        this(context, themeResId, null, -1, -1, -1);
        init(context);
    }

    public DatePickerDialogCustomDim(@NonNull Context context,
                                     @Nullable OnDateSetListener listener,
                                     int year,
                                     int month,
                                     int dayOfMonth) {
        this(context, 0, listener, year, month, dayOfMonth);
    }

    public DatePickerDialogCustomDim(@NonNull Context context,
                                     @StyleRes int themeResId,
                                     @Nullable OnDateSetListener listener,
                                     int year,
                                     int monthOfYear,
                                     int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        init(context);
    }

    private void init(Context context) {
        root = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        super.setOnDismissListener(dismissListener);
    }

    public void setDimAmount(@FloatRange(from = 0, to = 1f) float dim) {
        dimAmount = dim;
    }

    @Override
    public void show() {
        super.show();
        dimDrawable = new ColorDrawable(Color.GREEN); // a dim color
        dimDrawable.setBounds(0, 0, root.getWidth(), root.getHeight());

        ViewGroupOverlay overlay = root.getOverlay();
        overlay.add(dimDrawable);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(dimDrawable,
                PropertyValuesHolder.ofInt("alpha", 0, (int) (255 * dimAmount)));
        animator.setTarget(dimDrawable);
        animator.setDuration(animDuration);
        animator.start();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        outsideDismissListener = listener;
    }
}
