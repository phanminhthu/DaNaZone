package com.example.danazone04.danazone.ui.splash.main.contact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.main.base.take.fanpage.FanpageActivity;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import dmax.dialog.SpotsDialog;

@EFragment(R.layout.fragment_contact)
public class FragmentContact extends BaseContainerFragment {
    @ViewById
    WebView mWebView;
    private String myUrl = "http://xedapdana.com/danh-sach-chi-nhanh/";
    private Dialog waitingDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void afterViews() {
        waitingDialog = new SpotsDialog(getContext());
        setUpWebView();
    }

    private void setUpWebView() {
        mWebView.setWebViewClient(new MyBrWebView());
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
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
            waitingDialog.show();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            waitingDialog.dismiss();
        }
    }
}
