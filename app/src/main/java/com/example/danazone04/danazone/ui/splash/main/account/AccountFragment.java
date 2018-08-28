package com.example.danazone04.danazone.ui.splash.main.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
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
    @ViewById
    ImageView mImgAvatar;
    private Users users;
    private JSONArray jsonarray;

    @Override
    protected void afterViews() {
    }

    @Click(R.id.mTvSubmit)
    void onClick(View v) {
        UpdateAccountActivity_.intent(getContext())
                .users(users)
               .start();
    }


    private void loadData() {
        try {
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

                mTvName.setText(name);
                mTvPhone.setText(phone);
                mTvEmail.setText(email);
                mTvBirthDay.setText(birthday);
                mTvSex.setText(sex);
                mTvBike.setText(bike);
                if (avatar.equals("NULL")) {

                } else {
                    String path = "http://huyhoangdanang.com/danazone/images/" + avatar;
                    Picasso.with(getApplicationContext()).load(path).into(mImgAvatar);
                }

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

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog waitingDialog = new SpotsDialog(getContext());
        waitingDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Common.URL_INFORMATION, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                waitingDialog.dismiss();
                users = new Users();
                try {
                    jsonarray = new JSONArray(response);
                    loadData();
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
