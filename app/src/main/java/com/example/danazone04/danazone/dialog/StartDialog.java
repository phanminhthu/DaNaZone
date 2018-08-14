package com.example.danazone04.danazone.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseDialog;
import com.example.danazone04.danazone.R;

public class StartDialog extends BaseDialog implements View.OnClickListener {

    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onCallSerVice();
    }

    private OnDialogClickListener mListener;


    public StartDialog(Context context, OnDialogClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);
        TextView mTvCall = (TextView)findViewById(R.id.mTvSubmit);

        mImgCancelDialog.setOnClickListener(this);
        mTvCall.setOnClickListener(this);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_start;
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

