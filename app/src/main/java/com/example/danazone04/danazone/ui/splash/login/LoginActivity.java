package com.example.danazone04.danazone.ui.splash.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.common.Common;
import com.example.danazone04.danazone.common.MySingleton;
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity_;
import com.example.danazone04.danazone.utils.ConnectionUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

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
        if (!ConnectionUtil.isConnected(this)) {
            showAlertDialog("Vui lòng kiểm tra kết nối internet");
            return;
        }
    }

    @Click({R.id.mTvSubmit, R.id.mTvLogin})
    void onClick(View v) {
        if (ConnectionUtil.isConnected(this)) {
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
                    final AlertDialog waitingDialog = new SpotsDialog(LoginActivity.this);
                    waitingDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Common.URL_LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("thanhcong")) {
                                waitingDialog.dismiss();

                                SessionManager.getInstance().setKeySaveId(phone);
                                SessionManager.getInstance().setKeySavePass(password);
                                MainMenuActivity_.intent(LoginActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                                finish();
                            } else {
                                waitingDialog.dismiss();
                                showAlertDialog("Đăng nhập thất bại!");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            waitingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Có lỗi", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parms = new HashMap<>();
                            parms.put("phone", phone);
                            parms.put("password", password);
                            return parms;
                        }
                    };//ket thuc stringresquet
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                    break;
            }

        } else {
            showAlertDialog("Vui lòng kiểm tra kết nối internet");
        }
    }
}
