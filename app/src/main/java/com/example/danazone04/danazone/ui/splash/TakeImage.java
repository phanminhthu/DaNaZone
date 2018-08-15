package com.example.danazone04.danazone.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.google.android.gms.games.internal.game.Screenshot;

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
    @Override
    protected void afterView() {
        Toast.makeText(TakeImage.this, fileName, Toast.LENGTH_LONG).show();
         completePath = Environment.getExternalStorageDirectory() + "/" +fileName +".jpg";
        Glide.with(TakeImage.this).load(completePath).error(R.drawable.image1).into(iv_image);
    }

    @Click(R.id.mTvSubmit)
    void clickView(View v){
        Uri imageUri = Uri.parse(completePath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent , "Share"));
    }
}
