package com.keven.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import cube.com.axislibrary.factory.Axis;

/**
 * 滑动 LinearLayout
 */
public class SlideLinearLayout extends LinearLayout {
    private float mlastX = 0;
    private float mInitialDownX;
    private float mInitialDownY;
    private int mTouchSlop;

    public static final int MAX_WIDTH = Axis.scaleX(340);
    private Context mContext;
    private Scroller mScroller;
    private OnScrollListener mScrollListener;


    public static interface OnScrollListener {
        public void OnScroll(SlideLinearLayout view);
    }

    public SlideLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SlideLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        float x = (int) event.getX();
        float y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mInitialDownX = x;
                mInitialDownY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float xDiff = x - mInitialDownX;
                final float yDiff = y - mInitialDownY;
                if (Math.abs(xDiff) > mTouchSlop && Math.abs(xDiff) > Math.abs(yDiff)) {
                    result = true;
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    result = false;
                }
                break;
            }
        }
        mlastX = x;
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int maxLength = MAX_WIDTH;//dipToPx(mContext, MAX_WIDTH);
        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                int scrollX = this.getScrollX();
                Log.e("TAG", "MotionEvent.ACTION_MOVE scrollX:" + scrollX + "  mlastX=" + mlastX + "   x=" + x);
                int newScrollX = scrollX + (int) (mlastX - x);
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > maxLength) {
                    newScrollX = maxLength;
                }
                this.scrollTo(newScrollX, 0);
            }
            break;
            case MotionEvent.ACTION_UP: {
                int scrollX = this.getScrollX();
                Log.e("TAG", "MotionEvent.ACTION_UP scrollX:" + scrollX + "");
                int newScrollX;
                if (scrollX > maxLength / 2) {
                    newScrollX = maxLength;
                    mScrollListener.OnScroll(this);
                } else {
                    newScrollX = 0;
                }
                mScroller.startScroll(scrollX, 0, newScrollX - scrollX, 0);
                invalidate();
            }
            break;
        }
        mlastX = x;
        return true;
    }

    public void setOnScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        invalidate();
    }
}