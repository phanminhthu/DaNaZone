package com.example.danazone04.danazone.ui.splash.main.base.take.fanpage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_fanpage)

public class FanpageActivity extends BaseActivity {
    @ViewById
    WebView mWebView;
    private String myUrl = "https://www.facebook.com/dnmtbclub/?ref=br_tf";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        setUpWebView();
    }

    private void setUpWebView() {
        mWebView.setWebViewClient(new MyBrWebView());
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        //myWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.loadUrl(myUrl);
    }

    private class MyBrWebView extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
