package com.example.danazone04.danazone.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.games.internal.game.Screenshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@SuppressLint("Registered")
@EActivity(R.layout.activity_take_image)
public class TakeImage extends BaseActivity {
    @ViewById
    ImageView iv_image;
    @ViewById
    TextView mTvSubmit;
    @Extra
    String fileName;
    String completePath;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void afterView() {

        completePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/"+ fileName + ".jpg";
        System.out.println("00000000000000: " + completePath);
        Glide.with(TakeImage.this).load(completePath).error(R.drawable.image1).into(iv_image);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    @Click(R.id.mTvSubmit)
    void clickView(View v) {

//        Uri imageUri = Uri.parse(completePath);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpg");
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        startActivity(Intent.createChooser(intent , "Share"));
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(TakeImage.this, "ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        Picasso.with(getBaseContext()).load("http://chohoaviet.com/wp-content/uploads/2016/05/hoa-dong-tien-1.jpg").into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap).build();
                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto).build();
                    shareDialog.show(sharePhotoContent);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
