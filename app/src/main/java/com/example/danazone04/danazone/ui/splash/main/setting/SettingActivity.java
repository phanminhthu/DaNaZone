package com.example.danazone04.danazone.ui.splash.main.setting;

import android.annotation.SuppressLint;
import android.content.pm.PackageInstaller;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.dialog.ShareDialog;
import com.example.danazone04.danazone.ui.splash.main.metter.MetterActivity_;
import com.example.danazone04.danazone.ui.splash.main.setting.contact.ContactActivity_;
import com.example.danazone04.danazone.ui.splash.main.setting.outCome.OutComeActivity_;
import com.example.danazone04.danazone.ui.splash.main.setting.run.SettingRunActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

@SuppressLint("Registered")
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @ViewById
    LinearLayout mLlCoin;
    @ViewById
    LinearLayout mLlSetting;
    @ViewById
    LinearLayout mLlContact;


    @Override
    protected void afterView() {

    }

    @Click({R.id.mLlCoin, R.id.mLlSetting, R.id.mLlContact})
    void click(View v) {
        switch (v.getId()) {
            case R.id.mLlCoin:
                OutComeActivity_.intent(this).start();
                break;
            case R.id.mLlSetting:
                MetterActivity_.intent(this).start();
                break;
            case R.id.mLlContact:
                ContactActivity_.intent(this).start();
                break;
        }
    }
}
