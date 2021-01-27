package com.keven.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.keven.R;


public class WaveView extends View {

    // 默认属性值
    private static final int DEFAULT_AMPLITUDE = 200;
    private static final int DEFAULT_PERIOD = 16;
    private static final float DEFAULT_SPEED = .1F;
    private static final float DEFAULT_QUADRANT = .33F;
    private static final float DEFAULT_FREQUENCY = 1F / 360F;
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#660000FF");
    private static final int DEFAULT_WAVE_BK_COLOR = Color.parseColor("#EEEEEE");

    @SuppressWarnings("FieldCanBeLocal")
    @ColorInt
    private int mWaveColor;
    @ColorInt
    private int mWaveBKColor;
    // 振幅
    private int mAmplitude;
    // 波浪位于View的位置
    private float mQuadrant;
    // 波浪的频率,这个值越大,波浪越密集
    private float mFrequency;

    // 速度
    private float mSpeed;
    private float mShift;

    private final Paint mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mWavePath = new Path();

    private int dx;
    private int ViewWidth, ViewHeight;
    private Paint mWavePaint2 = new Paint();
    private Path mWavePath2 = new Path();
    private Canvas mWave2Canvas;
    private Bitmap mWave2Bitmap;
    private Drawable mWave2Drawable;


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet set) {
        final TypedArray array = context.obtainStyledAttributes(set, R.styleable.WaveView);
        mSpeed = array.getFloat(R.styleable.WaveView_speed, DEFAULT_SPEED);
        mWaveColor = array.getColor(R.styleable.WaveView_waveColor, DEFAULT_WAVE_COLOR);
        mWaveBKColor = array.getColor(R.styleable.WaveView_waveBackgroundColor, DEFAULT_WAVE_BK_COLOR);
        mAmplitude = array.getInt(R.styleable.WaveView_amplitude, DEFAULT_AMPLITUDE);
        mQuadrant = array.getFloat(R.styleable.WaveView_quadrant, DEFAULT_QUADRANT);
        mFrequency = array.getFloat(R.styleable.WaveView_frequency, DEFAULT_FREQUENCY);
        array.recycle();

        mWavePaint.setStrokeWidth(2);
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setShadowLayer(100, 0, 0, mWaveColor);

        mWavePaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mWavePaint2.setColor(Color.parseColor("#65ff0000"));
        mWavePaint2.setShadowLayer(100, 0, 0, Color.parseColor("#65ff0000"));

        addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int l, int t, int r, int b, int oldL, int oldT, int oldR, int oldB) {
                        removeOnLayoutChangeListener(this);
                        ViewWidth = getWidth(); // 获取宽度
                        ViewHeight = getHeight(); // 获取高度
                        mWave2Bitmap = Bitmap.createBitmap(ViewWidth, ViewHeight, Bitmap.Config.ARGB_8888);
                        mWave2Canvas = new Canvas(mWave2Bitmap);
                        mWave2Drawable = getContext().getResources().getDrawable(R.mipmap.blur_test);
                        mWave2Drawable.setBounds(0, 0, ViewWidth, ViewHeight);
                    }
                });

        postDelayed(new WaveAnimation(), DEFAULT_PERIOD);        // 开始波浪动画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth();
        final int height = getHeight();

        //--------------------------波浪1----------------------------
        final int waveHeight = (int) (getHeight() * mQuadrant);
        // 绘制背景
        canvas.drawColor(mWaveBKColor);
        mWavePath.moveTo(0, height);
        mWavePath.lineTo(0, waveHeight);
        for (int i = 1; i <= width; i++) {
            // 绘制正弦曲线 y = A Sin(ωt+ ρ) = A sin(2πft + ρ)  // 一个周期 2π
            final float y = (float) (waveHeight + mAmplitude * Math.sin(2 * Math.PI * i * mFrequency + mShift));
            mWavePath.lineTo(i, y);
        }
        // 将曲线闭合
        mWavePath.lineTo(width, height);

        int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00, 0xff00ffff};
        float[] pos = {0f, 0.2f, 0.4f, 0.6f, 1.0f};
        LinearGradient multiGradient = new LinearGradient(0, getHeight(), getWidth(), getHeight() , colors, pos, Shader.TileMode.REPEAT);
        Matrix matrix = new Matrix();
        matrix.setTranslate(-dx, 0);
        multiGradient.setLocalMatrix(matrix);
        mWavePaint.setShader(multiGradient);

        canvas.drawPath(mWavePath, mWavePaint);

        //--------------------------波浪2----------------------------
        mWavePath2.moveTo(-width + dx, height * 2 / 3);
        for (int i = 0; i < 3; i++) {
            mWavePath2.rQuadTo(width / 4, -100, width / 2, 0);   //和quadTo的API绝对位置不同的是rQuadTo的参数都是相对位置的值，相对上次最后一个点
            mWavePath2.rQuadTo(width / 4, 100, width / 2, 0);
        }
        mWavePath2.lineTo(width, height);
        mWavePath2.lineTo(0, height);
        mWavePath2.close();

        canvas.drawPath(mWavePath2, mWavePaint2);

        // ---- 图片为填充的波浪----
        /*mWave2Canvas.save();
        mWave2Canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mWave2Canvas.clipPath(mWavePath2);
        mWave2Drawable.draw(mWave2Canvas);
        canvas.drawBitmap(mWave2Bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
        mWave2Canvas.restore();*/
    }

    final class WaveAnimation implements Runnable {
        @Override
        public void run() {
            mWavePath.reset();
            mWavePath2.reset();
            mShift += mSpeed;
            int delta = (int)(mSpeed * 74.5);
            if (dx + delta > ViewWidth) {
                dx -= ViewWidth; // 一个周期走完，重新开始
            } else {
                dx += delta;
            }
            invalidate();
            WaveView.this.postDelayed(this, DEFAULT_PERIOD);
        }
    }
}