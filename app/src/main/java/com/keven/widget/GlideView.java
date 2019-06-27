package com.keven.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


public class GlideView extends View {

    private Context mContext;
    private String mUrl;
    private Bitmap bitmaps;


    public GlideView(Context context) {
        super(context);
        this.mContext = context;
    }

    public GlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public GlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setUrl(String url) {
        mUrl = url;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIcon(canvas);
    }

    public void drawIcon(Canvas canvas) {
        if (mUrl == null) return;

        if (bitmaps == null) {
            Glide.with(mContext).load(mUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource != null) {
                        bitmaps = resource;
                        postInvalidate();
                    }
                }
            });
        } else {
            Rect rect = new Rect(0, 0, bitmaps.getWidth(), bitmaps.getHeight());
            canvas.drawBitmap(bitmaps, rect, rect, null);
        }
    }
}