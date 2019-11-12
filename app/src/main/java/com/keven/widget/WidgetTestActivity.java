package com.keven.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.keven.R;
import com.keven.utils.PermissionUtil;
import com.keven.utils.PictureUtils;
import com.keven.widget.blurlibrary.BitmapBlur;
import com.keven.widget.blurlibrary.EasyBlur;

import java.io.IOException;

public class WidgetTestActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    @BindView(R.id.iv_cover) AutoLoadImageView iv_cover;
    @BindView(R.id.plurals_text) TextView plurals_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        iv_cover.setImageUrl("http://pic1.win4000.com/wallpaper/5/54055707675cb.jpg");
        plurals_text.setText(getResources().getQuantityString(R.plurals.orange, 10, 10));

        ((GlideView)findViewById(R.id.glide_view)).setUrl("http://pic1.win4000.com/wallpaper/5/54055707675cb.jpg");

        initTextureView();
        testBlur();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    //-------------------------------------------------------

    private Camera mCamera; //需要开camera权限
    private TextureView myTexture;

    private void initTextureView() {
        PermissionUtil.requestPermission(this,
                new String[]{Manifest.permission.CAMERA},
                new PermissionUtil.PermissionListener() {
                    public void onGranted() {
                        myTexture = (TextureView)findViewById(R.id.textureView1);
                        myTexture.setSurfaceTextureListener(WidgetTestActivity.this);
                    }
                },
                "请在设置中打开相机权限");
    }

    @SuppressLint("NewApi")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {
        mCamera = Camera.open();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        Log.d("TAG",  ">>"+previewSize.width + "#" + previewSize.height);
        myTexture.setLayoutParams(new LinearLayout.LayoutParams(previewSize.width/2, previewSize.height/2));
        try {
            mCamera.setPreviewTexture(arg0);
        } catch (IOException t) {
        }
        mCamera.startPreview();

        myTexture.setAlpha(1.0f);
        myTexture.setRotation(0.0f);
        myTexture.setClipToOutline(true);//开启裁剪
        myTexture.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                Rect selfRect = new Rect(0, 0, rect.right - rect.left + 100, rect.bottom - rect.top);
                outline.setRoundRect(selfRect, 100);
            }
        });
        /*Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f, 0, 0);
        myTexture.setTransform(matrix);*/
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
    }

    //-------------------------------------------------------

    private void testBlur() {
        final ImageView mImageBg = findViewById(R.id.image_bg);
        final ImageView view = findViewById(R.id.image_bg2);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.blur_test);
        mImageBg.setImageBitmap(bitmap);

        mImageBg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageBg.getViewTreeObserver().removeOnPreDrawListener(this);
                mImageBg.buildDrawingCache();
                Bitmap bmp = mImageBg.getDrawingCache();
                Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(overlay);
                canvas.translate(-view.getLeft(), view.getTop());
                canvas.drawBitmap(bmp, 0, 0, null);

                //方法一
                easyBlur(overlay, view);
                //方法二
                //bitmapBlur(overlay, view);
                return true;
            }
        });
    }

    private void easyBlur(Bitmap overlay, ImageView view) {
        long startTime = System.currentTimeMillis();

        Bitmap finalBitmap = EasyBlur.with(WidgetTestActivity.this)
                .bitmap(overlay) //要模糊的图片
                .scale(1)
                .radius(25)//模糊半径
                .policy(EasyBlur.BlurPolicy.RS_BLUR)
                .blur();

        long endTime = System.currentTimeMillis();
        Log.i("TAG","cost Time:"+(endTime - startTime));

        Bitmap finalBitmapRound = PictureUtils.roundBitmapByXfermode(finalBitmap, finalBitmap.getWidth(), finalBitmap.getHeight(), 80);
        view.setBackground(new BitmapDrawable(getResources(), finalBitmapRound));
        //view.setImageBitmap(finalBitmapRound);
    }

    private void bitmapBlur(final Bitmap overlay, final ImageView view) {
        BitmapBlur.addTask(overlay, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bitmap finalBitmap = (Bitmap) msg.obj;
                Bitmap finalBitmapRound = PictureUtils.roundBitmapByXfermode(finalBitmap, finalBitmap.getWidth(), finalBitmap.getHeight(), 80);
                view.setBackground(new BitmapDrawable(getResources(), finalBitmapRound));
                //view.setImageBitmap(finalBitmapRound);
                overlay.recycle();
            }
        });
    }
}