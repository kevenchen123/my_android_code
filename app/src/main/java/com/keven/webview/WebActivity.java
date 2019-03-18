package com.keven.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.keven.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TITLE = "extra.title";
    public static final String EXTRA_URL = "extra.url";

    protected static final int ANIMATION_TIME = 300;

    private String mUrl = "";
    private String mTitle = "";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title_text)
    TextView mWebTitle;
    @BindView(R.id.web_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.content_web_view)
    CustomWebView mCustomWebView;

    private CustomWebChromeClient mCustomWebChromeClient;


    public static Intent getStartIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TITLE)) {
            mTitle = intent.getStringExtra(EXTRA_TITLE);
        }
        if (intent.hasExtra(EXTRA_URL)) {
            mUrl = intent.getStringExtra(EXTRA_URL);
            Log.d("WebActivity", "url=" + mUrl);
        }
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mWebTitle.setText(mTitle);

        mCustomWebChromeClient = new CustomWebChromeClient();
        mCustomWebView.setWebChromeClient(mCustomWebChromeClient);
        mCustomWebView.setWebViewClient(new CustomWebClient());
        mCustomWebView.loadUrl(mUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    goPreviousOrBack();
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                goPreviousOrBack();
                break;
            case R.id.close_button:
                finish();
                break;
        }
    }

    private void goPreviousOrBack() {
        if (mCustomWebView.canGoBack()) {
            mCustomWebView.goBack();
        } else {
            mCustomWebView.onPause();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCustomWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCustomWebView.onPause();
        mCustomWebView.stopLoading();
    }


    private class CustomWebClient extends WebViewClient {
        private CustomWebClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("WebActivity", "url=" + url);
            if (url.startsWith("weixin://") || url.startsWith("alipays://")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String page) {
        }
    }


    private class CustomWebChromeClient extends WebChromeClient {
        private CustomWebChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mProgressBar != null) {
                mProgressBar.setProgress(newProgress);
                if (newProgress > 90 || newProgress == 100) {
                    hidProgressBar();
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(mTitle)) {
                mTitle = title;
            }
            if (mWebTitle != null) {
                mWebTitle.setText(mTitle);
            }
        }

        private void hidProgressBar() {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(ANIMATION_TIME);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mProgressBar.startAnimation(alphaAnimation);
        }
    }
}