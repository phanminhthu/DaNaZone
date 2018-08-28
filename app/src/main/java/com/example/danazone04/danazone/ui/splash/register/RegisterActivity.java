package com.example.danazone04.danazone.ui.splash.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
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
import com.example.danazone04.danazone.dialog.BikeDialog;
import com.example.danazone04.danazone.dialog.SexDialog;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
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
    @ViewById
    EditText mEdtEmail;
    @ViewById
    TextView mTvBirthDay;
    @ViewById
    TextView mTvSex;
    @ViewById
    TextView mTvBike;
    private int mYear, mMonth, mDay;

    @Override
    protected void afterView() {
        getSupportActionBar().hide();
    }

    @Click({R.id.mTvLogin, R.id.mTvSubmit, R.id.mTvBirthDay, R.id.mTvSex, R.id.mTvBike})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvLogin:
                LoginActivity_.intent(RegisterActivity.this).start();
                break;

            case R.id.mTvBirthDay:

                setUpDatePicker();
                break;

            case R.id.mTvSex:
                new SexDialog(RegisterActivity.this, new SexDialog.OnDialogClickListener() {
                    @Override
                    public void onMale() {
                        mTvSex.setText("Nam");
                    }

                    @Override
                    public void onFemale() {
                        mTvSex.setText("Nữ");
                    }
                }).show();
                break;

            case R.id.mTvBike:
                new BikeDialog(RegisterActivity.this, new BikeDialog.OnDialogClickListener() {
                    @Override
                    public void onDH() {
                        mTvBike.setText("Địa hình");
                    }

                    @Override
                    public void onRoad() {
                        mTvBike.setText("Road");
                    }

                    @Override
                    public void onTouring() {
                        mTvBike.setText("Touring");
                    }
                }).show();
                break;

            case R.id.mTvSubmit:
                final String username = mEdtName.getText().toString();
                final String phone = mEdtPhone.getText().toString().trim();
                final String password = mEdtPassWord.getText().toString().trim();
                final String email = mEdtEmail.getText().toString().trim();
                final String birthday = mTvBirthDay.getText().toString().trim();
                final String sex = mTvSex.getText().toString().trim();
                final String bike = mTvBike.getText().toString().trim();
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

                if (mTvBirthDay.getText().equals("Năm sinh")) {
                    showAlertDialog("Năm sinh không được để trống");
                    return;
                }
                if (mTvSex.getText().equals("Gới tính")) {
                    showAlertDialog("Giới tính không được để trống");
                    return;
                }
                if (mTvBike.getText().equals("Loại xe")) {
                    showAlertDialog("Vui lòng chọn loại xe");
                    return;
                }


                final AlertDialog waitingDialog = new SpotsDialog(RegisterActivity.this);
                waitingDialog.show();


                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Common.URL_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("thanhcong")) {
                            waitingDialog.dismiss();
                            SessionManager.getInstance().setKeySaveId(phone);
                            SessionManager.getInstance().setKeySavePass(password);
                            SessionManager.getInstance().setKeyName(username);

                            MainMenuActivity_.intent(RegisterActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                            finish();
                        } else {
                            waitingDialog.dismiss();
                            showAlertDialog("Đăng ký thất bại!");
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
                        parms.put("email", email);
                        parms.put("birthday", birthday);
                        parms.put("sex", sex);
                        parms.put("bike", bike);

                        return parms;
                    }
                };//ket thuc stringresquet
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                break;
        }
    }

    /**
     * setup date piker in register
     */
    private void setUpDatePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mTvBirthDay.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}

