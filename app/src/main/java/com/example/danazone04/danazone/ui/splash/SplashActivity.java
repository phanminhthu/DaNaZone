package com.example.danazone04.danazone.ui.splash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;

import com.example.danazone04.danazone.ui.splash.main.MainActivity_;

import com.example.danazone04.danazone.ui.splash.register.RegisterActivity_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@SuppressLint("Registered")
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @ViewById
    ImageView mImgLogo;
    private Handler mHandler = new Handler();

    @Override
    protected void afterView() {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(400);
        mAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mImgLogo.startAnimation(mAnimation);

        if (!SessionManager.getInstance().getKeySaveId().equals("") && !SessionManager.getInstance().getKeySavePass().equals("")) {
            MainActivity_.intent(SplashActivity.this).start();
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
}