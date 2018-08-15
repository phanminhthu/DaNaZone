package com.example.danazone04.danazone.ui.splash.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
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
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;

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
                if(username.equals("")){
                    mEdtName.requestFocus();
                    showAlertDialog("Tên không được để trống!");
                    return;
                }
                if(phone.equals("")){
                    mEdtPhone.requestFocus();
                    showAlertDialog("Số điện thoại không được để trống!");
                    return;
                }
                if(password.equals("")){
                    mEdtPassWord.requestFocus();
                    showAlertDialog("Mật khẩu không được để trống!");
                    return;
                }

                final AlertDialog waitingDialog = new SpotsDialog(RegisterActivity.this);
                waitingDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Common.URL_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("thanhcong")) {
                            SessionManager.getInstance().setKeySaveId(phone);
                            SessionManager.getInstance().setKeySavePass(password);
                            waitingDialog.dismiss();
                            MainActivity_.intent(RegisterActivity.this).start();
                        } else {
                            waitingDialog.dismiss();
                            Toast.makeText(getApplicationContext(), " Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
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
                        parms.put("username", username);
                        parms.put("phone", phone);
                        parms.put("password", password);

                        return parms;
                    }
                };//ket thuc stringresquet
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                break;
        }
    }

}
