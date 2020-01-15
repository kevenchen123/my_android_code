package com.keven.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;


public class ViewUtils {
    /**
     * Set a View height in pixels.
     *
     * @param v      The view to set the height.
     * @param height The height in pixels.
     */
    public static void setViewHeight(View v, int height) {
        final ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = height;
        v.requestLayout();
    }

    /**
     * Set a View width in pixels.
     *
     * @param v     The view to set the width.
     * @param width The width in pixels.
     */
    public static void setViewWidth(View v, int width) {
        final ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = width;
        v.requestLayout();
    }

    /**
     * Set a View margin top.
     *
     * @param v         The View to set the margin top.
     * @param marginTop the margin top in pixles
     */
    public static void setViewMarginTop(View v, int marginTop) {
        setViewMargin(v, 0, marginTop, 0, 0);
    }

    /**
     * Set a View margin.
     *
     * @param v      The view to set margins.
     * @param left   The margin at left in pixels
     * @param top    The margin at top in pixels
     * @param right  The margin at right in pixels
     * @param bottom The margin at bottom in pixels
     */
    public static void setViewMargin(View v, int left, int top, int right, int bottom) {
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        v.requestLayout();
    }

    //获取view在窗口中位置
    public static int getLocationInYAxis(View v) {
        final int[] globalPos = new int[2];
        v.getLocationInWindow(globalPos);
        return globalPos[1];
    }

    public static int getLocationInXAxis(View v) {
        final int[] globalPos = new int[2];
        v.getLocationInWindow(globalPos);
        return globalPos[0];
    }

    //---------------------------view 动画--------------------------

    public static void viewTransYAnim(View view, int translation, long animDuration) {
        view.animate()
                .translationY(translation)
                .setDuration(animDuration)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //view.setTranslationY(0);
                    }
                });
    }

    public static void viewTransXAnim(View view, int translation, long animDuration) {
        view.animate()
                .translationX(translation)
                .setDuration(animDuration)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //view.setTranslationX(0);
                    }
                });
    }


    public static void floatValueAnimator(float startValue, float endValue, int mAnimationDuration, OnInteractListener interactListener) {
        ValueAnimator animation = ValueAnimator.ofFloat(startValue, endValue);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(mAnimationDuration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                interactListener.onUpdate((float) animation.getAnimatedValue());;
            }
        });
        animation.start();
    }

    public interface OnInteractListener {
        void onUpdate(float val);
    }
}