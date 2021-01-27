package com.keven.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WrapContentHeightViewPager extends ViewPager {
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    getChildMeasureSpec(heightMeasureSpec, 0, child.getLayoutParams().height));// getChildMeasureSpec获取到child具体的高度
            int h = child.getMeasuredHeight();
            //采用最大的view的高度。
            if (h > height) {
                height = h;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}