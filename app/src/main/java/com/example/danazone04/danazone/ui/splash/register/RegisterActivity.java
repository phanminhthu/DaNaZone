package com.example.danazone04.danazone.ui.splash.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.common.Common;
import com.example.danazone04.danazone.common.MySingleton;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;
import com.example.danazone04.danazone.ui.splash.main.start.StartActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

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
                final String username = mEdtName.getText().toString();
                final String phone = mEdtPhone.getText().toString();
                final String password = mEdtPassWord.getText().toString();
                if (username.equals("")) {
                    mEdtName.requestFocus();
                    showAlertDialog("Tên không được để trống!");
                    return;
                }
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

                StartActivity_.intent(RegisterActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                finish();
                break;
        }
    }

}
