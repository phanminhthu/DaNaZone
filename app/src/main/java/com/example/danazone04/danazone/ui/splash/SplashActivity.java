package com.example.danazone04.danazone.ui.splash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.util.Base64;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;

import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@SuppressLint("Registered")
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @ViewById
    ImageView mImgLogo;
    private Handler mHandler = new Handler();

    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(400);
        mAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mImgLogo.startAnimation(mAnimation);
        printKeyHash();
        if (!SessionManager.getInstance().getKeySaveId().equals("") && !SessionManager.getInstance().getKeySavePass().equals("")) {
            MainMenuActivity_.intent(SplashActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK).start();
            finish();
        } else {
            Runnable mActivityStarter = new Runnable() {
                @Override
                public void run() {
                    RegisterActivity_.intent(SplashActivity.this).start();
                }
            };
            mHandler.postDelayed(mActivityStarter, 2000);
        }
    }

    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.danazone04.danazone",
                    getPackageManager().GET_SIGNATURES);
            for(Signature signature : packageInfo.signatures){
                MessageDigest ms = null;
                try {
                    ms = MessageDigest.getInstance("SHA");
                    System.out.println("111111111111111: " + Base64.encodeToString(ms.digest(), Base64.DEFAULT));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                ms.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}