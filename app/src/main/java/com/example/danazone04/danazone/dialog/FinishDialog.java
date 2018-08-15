package com.example.danazone04.danazone.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseDialog;
import com.example.danazone04.danazone.R;

public class FinishDialog extends BaseDialog implements View.OnClickListener {
    private Bitmap bitmapStart, bitmapEnd;
    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onCallSerVice();
    }

    private OnDialogClickListener mListener;


    public FinishDialog(Context context,Bitmap bitmapStart, Bitmap bitmapEnd, OnDialogClickListener listener) {
        super(context);
        mListener = listener;
        this.bitmapStart = bitmapStart;
        this.bitmapEnd = bitmapEnd;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);
        ImageView mImgStart = (ImageView) findViewById(R.id.mImgStart);
        ImageView mImgEnd = (ImageView) findViewById(R.id.mImgEnd);
        TextView mTvCall = (TextView)findViewById(R.id.mTvSubmit);
        LinearLayout ln = (LinearLayout) findViewById(R.id.mLinearProfile) ;

        mImgCancelDialog.setOnClickListener(this);
        mTvCall.setOnClickListener(this);
        mImgStart.setImageBitmap(bitmapStart);
        mImgEnd.setImageBitmap(bitmapEnd);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_finish;
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

