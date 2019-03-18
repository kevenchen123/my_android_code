package com.keven.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.keven.R;


/**
 * Created by zhoujiaqi on 2017/9/21.
 * 环形进度条按钮
 */
public class CircleProgressButton extends View {
    private static final String TAG = "CircleProgressButton";

    private int mMaxProgress = 5000;
    private int mProgress = 0;

    private final int mCircleLineStrokeWidth = 4;
    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;
    private final Paint mPaintCircle;

    private final Context mContext;

    private String mTxtHint1;
    private String mTxtHint2;
    private SweepGradient mSweepGradient;

    private CBOnTriggerListener cbOnTriggerListener;
    private OnUpdateProgressListener progressListener;

    private boolean hasTrigle = false;

    public boolean isControlButtonRun = false;

    public int circleBackColor = 0xFF141414;//0xFF006CFF
    public int cicleColor = 0xFF0586FB;

    private Runnable runnable;
    private Runnable Stoprunnable;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public CircleProgressButton(Context context) {
        this(context, null);
    }

    public CircleProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressButton);
            if (typedArray.hasValue(R.styleable.CircleProgressButton_circle_back_color)) {
                circleBackColor = typedArray.getColor(R.styleable.CircleProgressButton_circle_back_color, 0xFF141414);
            }
            typedArray.recycle();
        }

        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaintCircle = new Paint();

        runnable = new Runnable() {
            public void run() {
                //sendContinueMessage();
                mProgress += 20;
                setProgress(mProgress);
                handler.postDelayed(this, 20);
                if (mProgress == 5000) {
                    stop(controlSuccess);
                }
            }
        };

        Stoprunnable = new Runnable() {
            public void run() {
                mProgress += 260;
                setProgress(mProgress);
                handler.postDelayed(this, 10);
                if (mProgress == 5000) {
                    stop(controlSuccess);
                    hasTrigle = true;
                }
            }
        };
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        //canvas.drawColor(0xFFFFFF00);
        mSweepGradient = new SweepGradient(width / 2, height / 2, new int[]{cicleColor, cicleColor, cicleColor, cicleColor, cicleColor}, null);
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);

        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Style.STROKE);
        // 位置
        mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
        mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
        mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角x
        mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y

        // 绘制圆圈，进度条背景
        //canvas.drawArc(mRectF, -90, 360, false, mPaint);
        //mPaint.setColor(Color.rgb(0xf8, 0x60, 0x30));
        canvas.rotate(0, width / 2, height / 2);
        mPaint.setShader(mSweepGradient);
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        mPaintCircle.setColor(circleBackColor);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Style.FILL);
        canvas.drawCircle(width / 2, height / 2, (width / 2) - mCircleLineStrokeWidth, mPaintCircle);
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }

    public void setCBOnTriggerListener(CBOnTriggerListener listener) {
        cbOnTriggerListener = listener;
    }

    public interface CBOnTriggerListener {
        void trigger();
    }

    public interface OnUpdateProgressListener {
        void finish();
    }

    public void setOnUpdateProgressListener(OnUpdateProgressListener listener) {
        this.progressListener = listener;
    }

    public void start() {
        cicleColor = 0xFF0586FB;
        if (!isControlButtonRun) {
            isControlButtonRun = true;
            handler.postDelayed(runnable, 0);
        }
    }

    boolean controlSuccess = true;
    int controlTimeDelay = 1000;

    public void stop(boolean controlSuccess) {
        this.controlSuccess = controlSuccess;

        handler.removeCallbacks(runnable);
        handler.postDelayed(Stoprunnable, 0);

        if (controlSuccess) {
            cicleColor = 0xFF0586FB;//0xFF006CFF
            //controlTimeDelay = 3000;
        } else {
            cicleColor = 0xFFFF0000;//0xFF006CFF
            //controlTimeDelay = 6000;
        }

        if (cbOnTriggerListener != null) {
            if (hasTrigle) {
                if (progressListener != null) {
                    progressListener.finish();
                }
                hasTrigle = false;
                cbOnTriggerListener.trigger();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isControlButtonRun = false;
                setProgress(0);
                handler.removeCallbacks(Stoprunnable);
            }
        }, controlTimeDelay);
    }


    public void setCircleBackColor(int circleBackColor) {
        this.circleBackColor = circleBackColor;
        invalidate();
    }

    public boolean isControlButtonRun() {
        return isControlButtonRun;
    }

    public void setControlButtonRun(boolean controlButtonRun) {
        isControlButtonRun = controlButtonRun;
    }
}
