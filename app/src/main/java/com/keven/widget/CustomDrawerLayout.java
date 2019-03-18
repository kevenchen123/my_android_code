package com.keven.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * 自定义滑动边界的抽屉布局
 * Created by Administrator on 2016/8/30.
 */
public class CustomDrawerLayout extends DrawerLayout {
    private float mYLimit = 0;
    private boolean mIsYLimit = false;

    public CustomDrawerLayout(Context context) {
        super(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (mIsYLimit && !isDrawerVisible(Gravity.LEFT)) {
                if (ev.getY() > mYLimit)
                    ev.setAction(MotionEvent.ACTION_CANCEL);
            }
        } catch (Exception e) {
        }
        return super.onTouchEvent(ev);
    }

    public void setYLimit(float yLimit) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mYLimit = (yLimit * scale + 0.5f);
        mIsYLimit = true;
    }

    public void removeYLimit() {
        mYLimit = 0;
        mIsYLimit = false;
    }

    /**
     * 触摸事件拦截器
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (mIsYLimit && !isDrawerVisible(Gravity.LEFT)) {
                if (ev.getY() > mYLimit)
                    ev.setAction(MotionEvent.ACTION_CANCEL);
            }
        } catch (Exception e) {
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setDrawerLeftEdgeLength(float displayWidth) {
        try {
            Field leftDraggerField = DrawerLayout.class.getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(this);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            // Point displaySize = new Point();
            final float scale = getContext().getResources().getDisplayMetrics().density;
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displayWidth * scale + 0.5f)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
        }
    }
}