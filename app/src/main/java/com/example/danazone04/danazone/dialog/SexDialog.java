package com.example.danazone04.danazone.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseDialog;
import com.example.danazone04.danazone.R;

import java.io.IOException;

public class SexDialog extends BaseDialog implements View.OnClickListener {
    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onMale();
        void onFemale();
    }

    private OnDialogClickListener mListener;


    public SexDialog(Context context, OnDialogClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);
        TextView mTvMale = (TextView)findViewById(R.id.mTvMale);
        TextView mTvFemale = (TextView)findViewById(R.id.mTvFemale);

        mImgCancelDialog.setOnClickListener(this);
        mTvMale.setOnClickListener(this);
        mTvFemale.setOnClickListener(this);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_date;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImgCancelDialog:
                dismiss();
                break;

            case R.id.mTvMale:
                mListener.onMale();
                dismiss();
                break;

            case R.id.mTvFemale:
                mListener.onFemale();
                dismiss();
                break;
        }
    }
}


