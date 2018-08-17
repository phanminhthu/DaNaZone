package com.example.danazone04.danazone.ui.splash.main.setting;

import android.annotation.SuppressLint;
import android.content.pm.PackageInstaller;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.dialog.ShareDialog;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

@SuppressLint("Registered")
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    @ViewById
    ImageView mImgEnter;
    @ViewById
    TextView mTvCoin;
    @ViewById
    TextView mTvCode;

    @Override
    protected void afterView() {
        String coin = SessionManager.getInstance().getKeySaveCoin();
        mTvCoin.setText(coin);
        mTvCode.setText(SessionManager.getInstance().getKeySaveCode());
        if (!mTvCoin.getText().toString().equals("0")) {
            if (mTvCoin.getText().toString().equals("2")) {
                new ShareDialog(SettingActivity.this, new ShareDialog.OnDialogClickListener() {
                    @Override
                    public void onCallSerVice() {
                        SessionManager.getInstance().updateCoin("0");
                        String a = randomString(5);
                        if (SessionManager.getInstance().getKeySaveCode().equals("")) {
                            SessionManager.getInstance().setKeySaveCode(a);
                        }
                        SessionManager.getInstance().updateCode(a);
                        mTvCode.setText(SessionManager.getInstance().getKeySaveCode());
                        mTvCoin.setText("0");
                    }
                }).show();
            }
        }
    }

    @Click(R.id.mImgEnter)
    void click(View v) {
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i <= len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
