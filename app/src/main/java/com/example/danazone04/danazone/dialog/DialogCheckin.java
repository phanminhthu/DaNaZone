package com.example.danazone04.danazone.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danazone04.danazone.BaseDialog;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.TakeImage;
import com.squareup.picasso.Picasso;

public class DialogCheckin extends BaseDialog implements View.OnClickListener {
    private Bitmap bitmap;
    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onCallSerVice();
    }

    private OnDialogClickListener mListener;


    public DialogCheckin(Context context,Bitmap bitmap, OnDialogClickListener listener) {
        super(context);
        mListener = listener;
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);
        ImageView mImgCheckin = (ImageView) findViewById(R.id.mImgCheckin);
        TextView mTvCall = (TextView)findViewById(R.id.mTvSubmit);

        mImgCancelDialog.setOnClickListener(this);
        mTvCall.setOnClickListener(this);
        mImgCheckin.setImageBitmap(bitmap);
       // Glide.with(getContext()).load(bitmap).error(R.drawable.image1).into(mImgCheckin);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_checkin;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImgCancelDialog:
                dismiss();
                break;

            case R.id.mTvSubmit:
                mListener.onCallSerVice();
                dismiss();
                break;
        }
    }
}


