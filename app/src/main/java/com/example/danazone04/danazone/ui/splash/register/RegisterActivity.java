package com.example.danazone04.danazone.ui.splash.register;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;
import com.example.danazone04.danazone.utils.ConnectionUtil;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;


import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

@SuppressLint("Registered")
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    private static final int REQUEST_CODE = 100;
    @ViewById
    TextView mTvLogin;
    @ViewById
    TextView mTvSubmit;
    @ViewById
    EditText mEdtName;
    @ViewById
    EditText mEdtProvince;
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
    private String password,email,birthday,sex,bike,username, province;
    private UIManager uiManager;
    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        if(!ConnectionUtil.isConnected(this)){
            showAlertDialog("Vui lòng kiểm tra kết nối internet");
            return;
        }
    }

    @Click({R.id.mTvLogin, R.id.mTvSubmit, R.id.mTvBirthDay, R.id.mTvSex, R.id.mTvBike})
    void onClick(View v) {
        if(ConnectionUtil.isConnected(this)) {
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
                    username = mEdtName.getText().toString();
                    province = mEdtProvince.getText().toString().trim();
                    password = mEdtPassWord.getText().toString().trim();
                    email = mEdtEmail.getText().toString().trim();
                    birthday = mTvBirthDay.getText().toString().trim();
                    sex = mTvSex.getText().toString().trim();
                    bike = mTvBike.getText().toString().trim();
                    if (username.equals("")) {
                        mEdtName.requestFocus();
                        showAlertDialog("Tên không được để trống!");
                        return;
                    }
                    if (province.equals("")) {
                        mEdtProvince.requestFocus();
                        showAlertDialog("Tỉnh thành không được để trống!");
                        return;
                    }

                    if (email.equals("") || !isEmailValid(email)) {
                        showAlertDialog("Email không trống hoặc không đúng định dạng!");
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


                    Intent intent = new Intent(RegisterActivity.this, AccountKitActivity.class);
                    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
                    configurationBuilder.setDefaultCountryCode("VN");
                    //  uiManager = new SkinManager(SkinManager.Skin., ContextCompat.getColor(this, R.color.colorMain));

                    // configurationBuilder.setUIManager(uiManager);
                    intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
                    startActivityForResult(intent, REQUEST_CODE);

                    break;
            }
        }else{
            showAlertDialog("Vui lòng kiểm tra kết nối internet");
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                showAlertDialog("" + result.getError().getErrorType().getMessage());
                return;
            } else if (result.wasCancelled()) {
               // showAlertDialog("Cancel");
            } else {
                if (result.getAccessToken() != null) {
                    AlertDialog waitingdialog = new SpotsDialog(this);
                    waitingdialog.show();
                    waitingdialog.setMessage("Loading...");
                    waitingdialog.setCancelable(false);
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            final String phone = account.getPhoneNumber().toString();
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
                                    parms.put("province", province);

                                    return parms;
                                }
                            };//ket thuc stringresquet
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                }
            }
        }
    }
}

