package com.example.danazone04.danazone.ui.splash.register;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewById
    TextView mTvLogin;
    @ViewById
    TextView mTvSubmit;
    @ViewById
    EditText mEdtName;
    @ViewById
    EditText mEdtPhone;
    @ViewById
    EditText mEdtPassWord;

    @Override
    protected void afterView() {

    }

    @Click({R.id.mTvLogin, R.id.mTvSubmit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvLogin:
                LoginActivity_.intent(RegisterActivity.this).start();
                break;

            case R.id.mTvSubmit:
                MainActivity_.intent(RegisterActivity.this).start();
                break;
        }
    }

}
