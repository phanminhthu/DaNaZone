package com.example.danazone04.danazone.ui.splash.main.setting;

import android.annotation.SuppressLint;
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

@SuppressLint("Registered")
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @ViewById
    ImageView mImgEnter;
    @ViewById
    TextView mTvCoin;

    @Override
    protected void afterView() {
        String coin = SessionManager.getInstance().getKeySaveCoin();
        mTvCoin.setText(coin);
        if(mTvCoin.getText().toString().equals("13")){
            new ShareDialog(SettingActivity.this, new ShareDialog.OnDialogClickListener() {
                @Override
                public void onCallSerVice() {

                }
            }).show();
        }
    }

    @Click(R.id.mImgEnter)
    void click(View v) {

    }
}
