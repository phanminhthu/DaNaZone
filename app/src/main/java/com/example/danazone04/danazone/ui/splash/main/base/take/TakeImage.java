package com.example.danazone04.danazone.ui.splash.main.base.take;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.main.base.take.fanpage.FanpageActivity_;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_take_image)
public class TakeImage extends BaseActivity {
    @ViewById
    ImageView iv_image;
    @ViewById
    TextView mTvSubmit;
    @ViewById
    TextView mTvShareFanpage;
    @ViewById
    TextView mTvOut;
    @Extra
    String fileName;
    String completePath;
    String c;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Bitmap bm;

    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        completePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/" + fileName + ".jpg";
        bm = StringToBitMap(completePath);

        Glide.with(TakeImage.this).load(completePath).error(R.drawable.image1).into(iv_image);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(getBaseContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Click({R.id.mTvSubmit, R.id.mTvShareFanpage, R.id.mTvOut})
    void clickView(View v) {
        switch (v.getId()) {
            case R.id.mTvSubmit:
                SharePhoto photo = new SharePhoto.Builder().setBitmap(BitmapFactory.decodeFile(completePath)).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                shareDialog.show(content);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(TakeImage.this, "Thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
                break;

            case R.id.mTvShareFanpage:
                FanpageActivity_.intent(this).start();
                break;

            case R.id.mTvOut:
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
                break;
        }
    }
}
