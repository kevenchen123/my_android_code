package com.keven.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInitialScale(1);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(false);

        setFocusable(true);//获取焦点
        requestFocus();

        WebSettings webSettings = getSettings();
        webSettings.setSupportZoom(true);//设置是否支持变焦
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webSettings.setJavaScriptEnabled(true); //支持JavaScript
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setDomStorageEnabled(true); //缓存 （ 远程web数据的本地化存储）
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//本地缓存

        webSettings.setBlockNetworkImage(false);//显示网络图像
        webSettings.setLoadsImagesAutomatically(true);//显示网络图像
        webSettings.setPluginState(WebSettings.PluginState.ON);//插件支持

        webSettings.setDatabaseEnabled(true);//开启数据库
        webSettings.setGeolocationEnabled(true);//定位
        webSettings.setGeolocationDatabasePath(context.getApplicationContext().getDir("database",Context.MODE_PRIVATE).getPath());//数据库

        //滑动监听
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (getScrollY() == 0) {
                } else {
                }
            }
        });
    }
}