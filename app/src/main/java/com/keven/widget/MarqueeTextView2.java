package com.hozon.speechassisttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/*
 * 每次设置新文字后，显示结尾部分
 * marquee 为启动跑马灯, 步长1/5个字，跑马灯前后之间隔3个字
 */
public class MarqueeTextView2 extends AppCompatTextView {

    private boolean showAtEnd = false;
    private String showText;
    private int textWidth;
    private float textSize;
    private volatile boolean isNeedReMeasure = false;
    private volatile boolean isMarquee = false;
    private volatile int startX;

    public MarqueeTextView2(Context context) {
        super(context);
        setShowText((String) getText(), false);
        marquee();
    }

    public MarqueeTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setShowText((String) getText(), false);
        marquee();
    }

    public MarqueeTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setShowText((String) getText(), false);
        marquee();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float textBaseY = getBaseline();
        if (isNeedReMeasure) {
            startX = showAtEnd ? getMeasuredWidth() - getTextWidth() : 0;
            if (getMeasuredWidth() > getTextWidth()) {
                canvas.drawText(showText, 0, textBaseY, getPaint());
            } else {
                canvas.drawText(showText, startX, textBaseY, getPaint());
            }
        }
        if (isMarquee || !isNeedReMeasure) {
            canvas.drawText(showText, startX - (int)(textSize/5), textBaseY, getPaint());
            canvas.drawText(showText, startX - (int)(textSize/5) + textWidth + (int)textSize*3, textBaseY, getPaint());
            if (isMarquee) marquee();
        }
        isNeedReMeasure = false;
    }

    /**
     * 获取文字宽度
     */
    private int getTextWidth() {
        Paint paint = getPaint();
        paint.setTextSize(getTextSize());
        paint.setColor(getCurrentTextColor());
        textWidth = (int) paint.measureText(showText);
        textSize = getTextSize();
        return textWidth;
    }

    public void setShowText(String str, boolean end) {
        isNeedReMeasure = true;
        showAtEnd = end;
        showText = str;
        setText(str);
    }

    public void marquee() {
        if (getMeasuredWidth() > getTextWidth()) {
            return;
        }
        isMarquee = true;
        startX = startX - (int)(textSize/5);
        if (startX < getMeasuredWidth() - textWidth - (int)textSize*3 - textWidth) { // 第二段重复文字也跑到最后了，拉回第一段
            startX = getMeasuredWidth() - getTextWidth();
        }
        postInvalidateDelayed(20);
    }

    public void stopMarquee() {
        isMarquee = false;
    }
}
