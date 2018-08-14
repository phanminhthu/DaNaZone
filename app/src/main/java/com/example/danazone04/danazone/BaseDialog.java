package com.example.danazone04.danazone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;

/**
 * Created by THUPM on 3/1/2018.
 */

public abstract class BaseDialog extends Dialog {

    private static final String TAG = BaseDialog.class.getSimpleName();

    protected abstract void onCreateView(Bundle savedInstanceState);

    protected abstract int getLayout();

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(getLayout());
        setCancelable(true);
        onCreateView(savedInstanceState);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            Log.e(TAG, "show: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * show message dialog
     *
     * @param msg
     */
    protected void showAlertDialog(@NonNull String msg) {
        try {
            new AlertDialog.Builder(getContext())
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * show alertDialog option
     *
     * @param msg
     * @param listener
     */
    protected void showAlertDialog(@NonNull String msg, final OnClickListener listener) {
        new AlertDialog.Builder(getContext())
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onClick(dialogInterface, i);
                        }
                        dialogInterface.dismiss();
                    }
                }).show();
    }

}

