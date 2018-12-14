package com.example.keven.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class WebImageView extends WebView {

    public static final int SCALE_TYPE_CENTER = 0;
    public static final int SCALE_TYPE_CENTER_CROP = 1;
    public static final int SCALE_TYPE_CENTER_INSIDE = 2;
    public static final int SCALE_TYPE_FIT_XY = 3;
    public static final int SCALE_TYPE_ASPECT_RATIO = 4;

    private int layout_width, layout_height;
    private int measureWidth, measureHeight;

    private int scaleType;
    private float aspectRatioX = 1f;
    private float aspectRatioY = 1f;

    private boolean allowScroll = false;
    private ViewGroup scrollParentView;
    private boolean reachLeft;
    private boolean reachRight;
    private boolean reachTop;
    private boolean reachBottom;
    private float mInitMotionX;
    private float mInitMotionY;

    private boolean allowScale = false;
    private int maxScale = 1;

    private Handler mHandler = null;
    private String localCacheFile = null;


    public WebImageView(Context context) {
        this(context, null);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mHandler = new MyHandler(this);
        getAttr(context, attrs);

        setBackgroundColor(0);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);

        WebSettings webSettings = getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);

        setInitialScale(1);

        addJavascriptInterface(new JavaScriptInterface(), "androidObject");
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebViewClient());
    }

    private void getAttr(Context context, AttributeSet attrs) {
        //layout_width = Integer.valueOf(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width"));
        //layout_height = Integer.valueOf(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height"));

        int[] attrsArray = new int[] {
            android.R.attr.layout_width, // 0
            android.R.attr.layout_height // 1
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        layout_width = ta.getLayoutDimension(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layout_height = ta.getLayoutDimension(1, ViewGroup.LayoutParams.MATCH_PARENT);

        ta.recycle();

        ta = context.obtainStyledAttributes(attrs, R.styleable.WebImageView);
        scaleType = ta.getInt(R.styleable.WebImageView_scaleType, 0);
        aspectRatioX = ta.getFloat(R.styleable.WebImageView_aspectRatioX, 1f);
        aspectRatioY = ta.getFloat(R.styleable.WebImageView_aspectRatioY, 1f);
        allowScroll = ta.getBoolean(R.styleable.WebImageView_allowScroll, false);
        allowScale = ta.getBoolean(R.styleable.WebImageView_allowScale, false);
        maxScale = ta.getInt(R.styleable.WebImageView_maxScale, 1);
        ta.recycle();

        Log.e("keven", "******  layout_width=" + layout_width + "  layout_height=" + layout_height + "  scaleType=" + scaleType
            + "  allowScroll=" + allowScroll + "  allowScale=" + allowScale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.e("keven", "onMeasure   "+(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)+" , "+measureWidth
            +" , "+(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY)+" , "+measureHeight);

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("keven", "onLayout   "+changed+" , "+l+" , "+t+" , "+r+" , "+b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("keven", "ACTION_DOWN ========================");
        if (scrollParentView != null) {
            scrollParentView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollParentView != null) {
            final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mInitMotionX = event.getX();
                    mInitMotionY = event.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    final float dx = event.getX() - mInitMotionX;
                    final float dy = event.getY() - mInitMotionY;

                    int touchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
                    if (Math.abs(dx) > touchSlop || Math.abs(dy) > touchSlop) {
                        boolean landscape = Math.abs(dx) >= Math.abs(dy);
                        Log.e("keven", "ACTION_MOVE =" + dx + " # " + dy + " , " + landscape + " , " + touchSlop);

                        if (landscape && (dx > 0 && reachLeft || dx < 0 && reachRight)
                            || !landscape && (dy > 0 && reachTop || dx < 0 && reachBottom)) {
                            scrollParentView.requestDisallowInterceptTouchEvent(false);
                            Log.e("keven", "ACTION_MOVE ========================");
                        }
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }


    final class JavaScriptInterface {
        /**
         * This is not called on the UI thread. Post a runnable to invoke loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void measureImage(final String width, final String height) {
            if (mHandler != null) {
                Message msg = new Message();
                msg.arg1= Integer.valueOf(width);
                msg.arg2= Integer.valueOf(height);
                mHandler.sendMessage(msg);
            }
        }

        @JavascriptInterface
        public void onScroll(final String reachLeft, final String reachRight, final String reachTop, final String reachBottom) {
            WebImageView.this.reachLeft = Boolean.parseBoolean(reachLeft);
            WebImageView.this.reachRight = Boolean.parseBoolean(reachRight);
            WebImageView.this.reachTop = Boolean.parseBoolean(reachTop);
            WebImageView.this.reachBottom = Boolean.parseBoolean(reachBottom);
            Log.e("keven", "onScroll  " + WebImageView.this.reachLeft + " , " + WebImageView.this.reachRight + " , " + WebImageView.this.reachTop + " , " + WebImageView.this.reachBottom);
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<WebImageView> mView;

        public MyHandler(WebImageView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            WebImageView webImageView = mView.get();
            if (webImageView != null) {

                // must set viewport before setting image size
                if (webImageView.allowScale) {
                    webImageView.loadUrl("javascript:changeMaxScale(" + webImageView.maxScale + ")");
                    WebSettings webSettings = webImageView.getSettings();
                    webSettings.setBuiltInZoomControls(true);
                }

                ViewGroup.LayoutParams ly = webImageView.getLayoutParams();
                try {
                    int srcWidth = msg.arg1;
                    int srcHeight = msg.arg2;
                    Log.e("keven", "measureImage   " + srcWidth+" , " + srcHeight);

                    ly.width =  webImageView.layout_width == ViewGroup.LayoutParams.WRAP_CONTENT ?
                                (srcWidth <= webImageView.measureWidth ? srcWidth : webImageView.measureWidth) : webImageView.measureWidth;
                    ly.height = webImageView.layout_height == ViewGroup.LayoutParams.WRAP_CONTENT ?
                                (srcHeight <= webImageView.measureHeight ? srcHeight : webImageView.measureHeight) : webImageView.measureHeight;

                    float ratioWidth = (float) ly.width / srcWidth;
                    float ratioHeight = (float) ly.height / srcHeight;
                    Log.e("keven", "measureImage   " + ly.width +" , " + ly.height +" , " + ratioWidth +" , " + ratioHeight);

                    // reset image size
                    switch (webImageView.scaleType) {
                        case SCALE_TYPE_CENTER: {
                            webImageView.loadUrl("javascript:changeImageSize(" + srcWidth +","+ srcHeight +","+ webImageView.allowScroll + ")");
                            break;
                        }
                        case SCALE_TYPE_CENTER_CROP: {
                            float ratio = ratioWidth > ratioHeight ? ratioWidth : ratioHeight;
                            webImageView.loadUrl("javascript:changeImageSize(" + (int)(srcWidth * ratio) +","+ (int)(srcHeight * ratio) +","+ webImageView.allowScroll + ")");
                            break;
                        }
                        case SCALE_TYPE_CENTER_INSIDE: {
                            float ratio = ratioWidth <= ratioHeight ? ratioWidth : ratioHeight;
                            webImageView.loadUrl("javascript:changeImageSize(" + (int)(srcWidth * ratio) +","+ (int)(srcHeight * ratio) +","+ webImageView.allowScroll + ")");
                            break;
                        }
                        case SCALE_TYPE_FIT_XY: {
                            webImageView.loadUrl("javascript:changeImageSize(" + (int)(srcWidth * ratioWidth) +","+ (int)(srcHeight * ratioHeight) +","+ webImageView.allowScroll + ")");
                            break;
                        }
                        case SCALE_TYPE_ASPECT_RATIO: {
                            webImageView.loadUrl("javascript:changeImageSize(" + (int)(srcWidth * webImageView.aspectRatioX) +","+ (int)(srcHeight * webImageView.aspectRatioY) +","+ webImageView.allowScroll + ")");
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                }
                webImageView.setLayoutParams(ly);
                webImageView.requestLayout();
            }
        }
    }


    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.e("keven", "onJsAlert  "+message);
            result.confirm();
            return true;
        }
    }

    final class MyWebViewClient extends WebViewClient {
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            Log.e("keven", "onScaleChanged  "+oldScale+"   "+newScale);
        }
    }


    public void setScrollParentView(ViewGroup scrollParentView) {
        this.scrollParentView = scrollParentView;
    }

    public void showImageUrl(String url) {
        String html = getHeadHtml()
            + String.format("<img class=\"img-responsive\" src=\"%s\"/></div></body></html>", url);

        loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    private String getHeadHtml() {
        String result = "";
        try {
            InputStream in = this.getResources().getAssets().open("head.html");
            if (in != null) {
                int length = in.available();
                byte[] buffer = new byte[length];
                if (in.read(buffer) > 0) result = new String(buffer, "UTF-8");
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void showLocalImage(int rid) {
        localCacheFile = getContext().getExternalCacheDir().toString() + File.separator + getClass().getSimpleName() + rid;
        LoadMainTask task = new LoadMainTask(this, localCacheFile);
        task.execute(getResources().openRawResource(rid));
    }

    private class LoadMainTask extends AsyncTask<InputStream, Object, WebImageView> {
        WebImageView view;
        String targetFile;

        public LoadMainTask(WebImageView view, String targetFile) {
            this.view = view;
            this.targetFile = targetFile;
        }

        protected WebImageView doInBackground(InputStream... fin) {
            try {
                FileOutputStream fos = new FileOutputStream(targetFile);
                int length;
                byte[] buffer = new byte[1024 * 32];
                while( (length = fin[0].read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
                fin[0].close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return view;
        }

        protected void onPostExecute(WebImageView view) {
            view.showImageUrl("file:///" +  targetFile);
        }
    }

    protected void finalize() {
        Log.e("keven", "finalization = " + this);
        if (localCacheFile != null && new File(localCacheFile).exists()) {
            Log.e("keven", "clear = " + localCacheFile);
            new File(localCacheFile).delete();
        }
    }
}