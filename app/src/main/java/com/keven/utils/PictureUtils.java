package com.keven.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PictureUtils {

    public static Drawable getDrawableFromNet(String url) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(url).openStream(), "src");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static Bitmap getBitmapFromPath(String rootPath, String imgName) {
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File image = new File(file.getAbsolutePath(), imgName);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    public static Bitmap readImage(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getAssets().open("image/" + name);
            if (is != null) {
                BitmapFactory.Options opt = null;
                opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                Bitmap img = BitmapFactory.decodeStream(is, null, opt);
                Matrix matrix = new Matrix();
                Bitmap newbm = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
                return newbm;
            }
        } catch (Exception e) {
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        return null;
    }


    // 将byte[]转换成InputStream
    public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    // 将InputStream转换成byte[]
    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将Bitmap转换成InputStream
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将InputStream转换成Bitmap
    public Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    // Bitmap转换成byte[]
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // byte[]转换成Bitmap
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    // Drawable转换成InputStream
    public InputStream Drawable2InputStream(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2InputStream(bitmap, 100);
    }

    // InputStream转换成Drawable
    public Drawable InputStream2Drawable(InputStream is) {
        Bitmap bitmap = this.InputStream2Bitmap(is);
        return this.bitmap2Drawable(bitmap);
    }

    // Drawable转换成byte[]
    public byte[] Drawable2Bytes(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2Bytes(bitmap);
    }

    // byte[]转换成Drawable
    public Drawable Bytes2Drawable(byte[] b) {
        Bitmap bitmap = this.Bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }

    // Drawable转换成Bitmap
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap转换成Drawable
    public Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }


    /**
     *为单个view设置点击效果，高版本带涟漪反馈
     * @param normalColor 未点击的颜色
     * @param pressColor  按下的颜色
     * @param view  目标view
     */
    public static void setOnclickfeedBack(int normalColor, int pressColor, View view) {
        Drawable bgDrawble;
        ColorDrawable drawablePressed = new ColorDrawable(pressColor);//分别解析两种颜色为colordrawble
        ColorDrawable drawableNormal = new ColorDrawable(normalColor);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {//高版本设置RippleDrawable 而低版本设置 StateListDrawable也就是selector
            ColorStateList stateList = ColorStateList.valueOf(pressColor);
            RippleDrawable rippleDrawable = new RippleDrawable(stateList, drawableNormal, drawablePressed);
            bgDrawble = rippleDrawable;
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
            stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, drawableNormal);
            bgDrawble = stateListDrawable;
        }
        view.setBackgroundDrawable(bgDrawble);//最终设置给我们的view作为背景
    }


    /**
     * 利用Xfermode绘制圆角图片
     *
     * @param bitmap    待处理图片
     * @param outWidth  结果图片宽度，一般为控件的宽度
     * @param outHeight 结果图片高度，一般为控件的高度
     * @param radius    圆角半径大小
     * @return 结果图片
     */
    public static Bitmap roundBitmapByXfermode(Bitmap bitmap, int outWidth, int outHeight, int radius) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }
        // 等比例缩放拉伸
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawARGB(0, 0, 0, 0);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, (float) outWidth, (float) outHeight);

        // 在画布上绘制圆角图
        canvas.drawRoundRect(rectF, radius, radius, paint);
        // 设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 在画布上绘制原图片
        Rect ret = new Rect(0, 0, outWidth, outHeight);
        canvas.drawBitmap(newBt, ret, ret, paint);
        return targetBitmap;
    }
}