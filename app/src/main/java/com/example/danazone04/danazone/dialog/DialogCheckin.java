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

public class DialogCheckin extends BaseDialog implements View.OnClickListener {
    private Bitmap bitmap;
    private String sl;
    private ExifInterface exifInterface;
    /**
     * Interface dialog click listener
     */
    public interface OnDialogClickListener {
        void onCallSerVice();
    }

    private OnDialogClickListener mListener;


    public DialogCheckin(Context context,Bitmap bitmap,String sl, OnDialogClickListener listener) {
        super(context);
        mListener = listener;
        this.bitmap = bitmap;
        this.sl = sl;
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ImageView mImgCancelDialog = (ImageView) findViewById(R.id.mImgCancelDialog);
        ImageView mImgCheckin = (ImageView) findViewById(R.id.mImgCheckin);
        TextView mTvCall = (TextView)findViewById(R.id.mTvSubmit);

        mImgCancelDialog.setOnClickListener(this);
        mTvCall.setOnClickListener(this);
        try {
            exifInterface = new ExifInterface(sl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int oritation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bm = rotateBitmap(bitmap, oritation);

        mImgCheckin.setImageBitmap(bm);
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

    public static Bitmap rotateBitmap(Bitmap bitmap, int oritation){
        Matrix matrix = new Matrix();
        switch (oritation){
            case ExifInterface.ORIENTATION_NORMAL:

                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1,1);
                return bitmap;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);

                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.setScale(-1,1);

                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;

            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.setScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;

            default:
                return  bitmap;


        }
        try{
            Bitmap bmRotate = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(),matrix, true);
            bitmap.recycle();
            return bmRotate;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
}


