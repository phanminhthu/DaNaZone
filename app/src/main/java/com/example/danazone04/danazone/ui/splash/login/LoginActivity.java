package com.example.danazone04.danazone.ui.splash.login;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)

public class LoginActivity extends BaseActivity {
    @ViewById
    TextView mTvSubmit;

    @Override
    protected void afterView() {

    }

    @Click(R.id.mTvSubmit)
    void onClick(View v){
        MainActivity_.intent(LoginActivity.this).start();
    }
}
