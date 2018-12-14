package com.keven.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInitialScale(1);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(false);

        WebSettings webSettings = getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }
}