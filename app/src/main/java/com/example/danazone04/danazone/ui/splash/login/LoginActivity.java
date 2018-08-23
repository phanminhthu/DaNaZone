package com.example.danazone04.danazone.ui.splash.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity_;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)

public class LoginActivity extends BaseActivity {
    @ViewById
    TextView mTvSubmit;
    @ViewById
    TextView mEdtPhone;
    @ViewById
    TextView mEdtPassWord;
    @ViewById
    TextView mTvLogin;

    @Override
    protected void afterView() {
    getSupportActionBar().hide();
    }

    @Click({R.id.mTvSubmit, R.id.mTvLogin})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvLogin:
                RegisterActivity_.intent(LoginActivity.this).start();
                break;
            case R.id.mTvSubmit:
                final String phone = mEdtPhone.getText().toString();
                final String password = mEdtPassWord.getText().toString();

                if (phone.equals("")) {
                    mEdtPhone.requestFocus();
                    showAlertDialog("Số điện thoại không được để trống!");
                    return;
                }
                if (password.equals("")) {
                    mEdtPassWord.requestFocus();
                    showAlertDialog("Mật khẩu không được để trống!");
                    return;
                }

                SessionManager.getInstance().setKeySaveId(phone);
                SessionManager.getInstance().setKeySavePass(password);
                MainMenuActivity_.intent(LoginActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                finish();
                break;
        }

    }
}
