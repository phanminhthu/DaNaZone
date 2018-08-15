package com.example.danazone04.danazone.ui.splash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
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
import com.example.danazone04.danazone.ui.splash.main.MainActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

@SuppressLint("Registered")
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @ViewById
    ImageView mImgLogo;
    private Handler mHandler = new Handler();

    @Override
    protected void afterView() {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(400);
        mAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mImgLogo.startAnimation(mAnimation);

        if (!SessionManager.getInstance().getKeySaveId().equals("") && !SessionManager.getInstance().getKeySavePass().equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Common.URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("thanhcong")) {
                        MainActivity_.intent(SplashActivity.this).start();
                    } else {
                        Toast.makeText(getApplicationContext(), " Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Có lỗi", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("phone", SessionManager.getInstance().getKeySaveId());
                    parms.put("password", SessionManager.getInstance().getKeySavePass());

                    return parms;
                }
            };//ket thuc stringresquet
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } else {
            Runnable mActivityStarter = new Runnable() {
                @Override
                public void run() {
                    RegisterActivity_.intent(SplashActivity.this).start();
                }
            };
            mHandler.postDelayed(mActivityStarter, 2000);
        }
    }
}