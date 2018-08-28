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

public class BikeDialog extends BaseDialog implements View.OnClickListener {
    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onDH();

        void onRoad();

        void onTouring();
    }

    private OnDialogClickListener mListener;


    public BikeDialog(Context context, OnDialogClickListener listener) {
        super(context);
        mListener = listener;

    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);

        TextView mTvDH = (TextView) findViewById(R.id.mTvDH);
        TextView mTvRoad = (TextView) findViewById(R.id.mTvRoad);
        TextView mTvTouring = (TextView) findViewById(R.id.mTvTouring);

        mImgCancelDialog.setOnClickListener(this);
        mTvDH.setOnClickListener(this);
        mTvRoad.setOnClickListener(this);
        mTvTouring.setOnClickListener(this);


    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_bike;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mImgCancelDialog:
                dismiss();
                break;

            case R.id.mTvDH:
                mListener.onDH();
                dismiss();
                break;

            case R.id.mTvRoad:
                mListener.onRoad();
                dismiss();
                break;

            case R.id.mTvTouring:
                mListener.onTouring();
                dismiss();
                break;
        }
    }
}


