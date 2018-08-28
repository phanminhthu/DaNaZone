package com.example.danazone04.danazone.ui.splash.main.account.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.danazone04.danazone.bean.Users;
import com.example.danazone04.danazone.common.Common;
import com.example.danazone04.danazone.common.MySingleton;
import com.example.danazone04.danazone.dialog.BikeDialog;
import com.example.danazone04.danazone.dialog.SexDialog;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.account.AccountFragment;
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dmax.dialog.SpotsDialog;

@SuppressLint("Registered")
@EActivity(R.layout.activity_update_account)
public class UpdateAccountActivity extends BaseActivity {
    @Extra
    Users users;

    @ViewById
    TextView mTvSubmit;
    @ViewById
    EditText mEdtName;
    @ViewById
    EditText mEdtPhone;
    @ViewById
    EditText mEdtEmail;
    @ViewById
    TextView mTvBirthDay;
    @ViewById
    TextView mTvSex;
    @ViewById
    TextView mTvBike;
    @ViewById
    ImageView mImgAvatar;
    private int mYear, mMonth, mDay;
    private GalleryPhoto galleryPhoto;
    private final int GALLERY_REQUEST = 22131;
    private Bitmap bitmap;
    private File file;
    private String selectPhoto, url, kq;
    private int iduser;
    private String mUlrImage;

    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        galleryPhoto = new GalleryPhoto(this);
        mEdtName.setText(users.getUsername());
        mEdtPhone.setText(users.getPhone());
        mEdtEmail.setText(users.getEmail());
        mTvBirthDay.setText(users.getBirthday());
        mTvSex.setText(users.getSex());
        mTvBike.setText(users.getBike());
        iduser = users.getId();
        if (users.getAvatar().equals("NULL")) {

        } else {
            String path = "http://huyhoangdanang.com/danazone/images/" + users.getAvatar();
            Picasso.with(getApplicationContext()).load(path).into(mImgAvatar);
        }
    }

    @Click({R.id.mTvSubmit, R.id.mTvBirthDay, R.id.mTvSex, R.id.mTvBike, R.id.mImgAvatar})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImgAvatar:
                Intent in = galleryPhoto.openGalleryIntent();
                startActivityForResult(in, GALLERY_REQUEST);
                break;

            case R.id.mTvBirthDay:
                setUpDatePicker();
                break;

            case R.id.mTvSex:
                new SexDialog(UpdateAccountActivity.this, new SexDialog.OnDialogClickListener() {
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
                new BikeDialog(UpdateAccountActivity.this, new BikeDialog.OnDialogClickListener() {
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
                kq = (String.valueOf(Random()));

                final String username = mEdtName.getText().toString();
                final String phone = mEdtPhone.getText().toString().trim();
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

                final AlertDialog waitingDialog = new SpotsDialog(UpdateAccountActivity.this);
                waitingDialog.show();

                if(selectPhoto == null){
                    mUlrImage = users.getAvatar();
                }else{
                    mUlrImage = file.getName().toString().replace(file.getName().toString(), gio() + kq + ".jpeg");
                }

                try {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Common.URL_UPDATE_INFORMATION, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("thanhcong")) {
                                waitingDialog.dismiss();
                                SessionManager.getInstance().updateSaveId(phone);
                                finish();
                            } else {
                                waitingDialog.dismiss();
                                showAlertDialog("Lỗi!");
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
                            parms.put("iduser", String.valueOf(iduser));
                            parms.put("username", username);
                            parms.put("phone", phone);
                            parms.put("avatar", mUlrImage);
                            parms.put("email", email);
                            parms.put("birthday", birthday);
                            parms.put("sex", sex);
                            parms.put("bike", bike);

                            return parms;
                        }
                    };//ket thuc stringresquet
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                    if(selectPhoto !=null) {
                        bitmap = ImageLoader.init().from(selectPhoto).requestSize(1024, 1024).getBitmap();
                        final String encodeIamge = ImageBase64.encode(bitmap);

                        // String url1 = "http://pmthu.esy.es/dulichviet/uplavatar.php";
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Common.URL_IMAGE_TO_SERVER, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(MainActivity.this,response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Toast.makeText(CapNhatFragment.this.getActivity(),"loi upload", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("i", encodeIamge);
                                params.put("r", kq);
                                return params;
                            }
                        };
                        MySingleton.getInstance(this).addToRequestQueue(stringRequest1);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();
                selectPhoto = photoPath;
                file = new File(photoPath);
                url = file.getName().toString();

                try {
                    Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    mImgAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Loi anh", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String gio() {
        Date date = new Date();
        SimpleDateFormat formats = new SimpleDateFormat("yyyyMMdd");
        String daynew = formats.format(date);
        return daynew;
    }

    public int Random() {
        //tong tu 10 den 19
        Random rand = new Random();
        int num = rand.nextInt(10000000);
        return num;

    }
}
