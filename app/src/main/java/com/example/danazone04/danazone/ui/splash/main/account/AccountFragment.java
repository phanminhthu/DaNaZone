package com.example.danazone04.danazone.ui.splash.main.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.bean.Users;
import com.example.danazone04.danazone.common.Common;
import com.example.danazone04.danazone.common.MySingleton;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity;
import com.example.danazone04.danazone.ui.splash.main.account.update.UpdateAccountActivity;
import com.example.danazone04.danazone.ui.splash.main.account.update.UpdateAccountActivity_;
import com.example.danazone04.danazone.ui.splash.main.menu.MainMenuActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

@EFragment(R.layout.fragment_account)
public class AccountFragment extends BaseContainerFragment {
    @ViewById
    TextView mTvSubmit;
    @ViewById
    TextView mTvName;
    @ViewById
    TextView mTvPhone;
    @ViewById
    TextView mTvEmail;
    @ViewById
    TextView mTvBirthDay;
    @ViewById
    TextView mTvSex;
    @ViewById
    TextView mTvBike;

    private Users users;

    @Override
    protected void afterViews() {
        final AlertDialog waitingDialog = new SpotsDialog(getContext());
        waitingDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Common.URL_INFORMATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                waitingDialog.dismiss();
                System.out.println("55555555555: " + response);
                 users = new Users();

                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String name = jsonobject.getString("username");
                        String phone = jsonobject.getString("phone");
                        String avatar = jsonobject.getString("avatar");
                        String email = jsonobject.getString("email");
                        String birthday = jsonobject.getString("birthday");
                        String sex = jsonobject.getString("sex");
                        String bike = jsonobject.getString("bike");
                        String iduser = jsonobject.getString("iduser");

                        System.out.println("2222222222222222222: " + avatar);

                        mTvName.setText(name);
                        mTvPhone.setText(phone);
                        mTvEmail.setText(email);
                        mTvBirthDay.setText(birthday);
                        mTvSex.setText(sex);
                        mTvBike.setText(bike);
                        // mCoin = mTvCoin.getText().toString();

                        users.setAvatar(avatar);
                        users.setBike(bike);
                        users.setBirthday(birthday);
                        users.setSex(sex);
                        users.setUsername(name);
                        users.setEmail(email);
                        users.setPhone(phone);
                        users.setId(Integer.valueOf(iduser));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
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
                parms.put("phone", SessionManager.getInstance().getKeySaveId());
                parms.put("password", SessionManager.getInstance().getKeySavePass());
                return parms;
            }
        };//ket thuc stringresquet
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Click(R.id.mTvSubmit)
    void onClick(View v) {
        UpdateAccountActivity_.intent(getContext()).users(users).start();
    }
}
