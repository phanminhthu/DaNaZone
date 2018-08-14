package com.example.danazone04.danazone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by PC on 1/17/2018.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity implements BaseActivityListener {
    protected final String TAG = this.getClass().getSimpleName();

    private PermissionRequestCallback mPermissionsCallBack;
    private ProgressDialog mProgressDialog;
    private int mPermissionRequestCode = 1;
    private Handler mHandlerProcess = new Handler();


    protected abstract void afterView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handler SCREEN_ORIENTATION_PORTRAIT

        intProgressBar();

    }



    @AfterViews
    protected void initView() {
        this.afterView();
        //ApiCallBack.setOnServiceErrorListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApp.getInstance().activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseApp.getInstance().activityPaused();

    }

    @Override
    protected void onDestroy() {
//        try {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//                mProgressDialog = null;
//            }
//
//            if (mDialog != null && mDialog.isShowing()) {
//                mDialog.dismiss();
//                mDialog = null;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "" + e.getMessage());
//        }

        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * Init dialog.
     */
    private void intProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("waiting");
        mProgressDialog.setCancelable(false);

        //mDialog = new PFLoadingDialog(this);
    }

    /**
     * show dialog message.
     */
    public void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.show();
        }

        // dismiss process bar about 10 second
        mHandlerProcess.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
            }
        }, 30000);
    }

    /**
     * show dialog not message.
     */
//    public void showDialog() {
//        if (mDialog != null && !mDialog.isShowing() && !isFinishing()) {
//            mDialog.show();
//        }
//
//        // dismiss process bar about 10 second
//        mHandlerProcess.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dismissDialog();
//            }
//        }, 30000);
//    }
    @Override
    public void showMessage(String message) {
        showMessage(null, message);
    }

    /**
     * dismiss message dialog
     */
    public void dismissProgressDialog() {
        if (!isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * dismiss dialog
     */
//    public void dismissDialog() {
//        if (!isFinishing() && mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
//    }

    /**
     * show message dialog
     *
     * @param msg
     */
    protected void showAlertDialog(@NonNull String msg) {
        try {
            new AlertDialog.Builder(this)
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
    protected void showAlertDialog(@NonNull String msg, final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onClick(dialogInterface, i);
                        }
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    /**
     * show dialog
     *
     * @param resId
     */
    protected void showAlertDialog(@StringRes int resId) {
        showAlertDialog(getString(resId));
    }

    /**
     * permission
     *
     * @param permission
     * @param callback
     */
    @Override
    public void onRequestPermission(String permission, PermissionRequestCallback callback) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            callback.onGrant(permission);
            return;
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                callback.onDeny(true);
            } else {
                mPermissionRequestCode++;
                mPermissionsCallBack = callback;
                ActivityCompat.requestPermissions(this, new String[]{permission}, mPermissionRequestCode);
            }
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            callback.onDeny(true);
        } else {
            mPermissionRequestCode++;
            mPermissionsCallBack = callback;
            ActivityCompat.requestPermissions(this, new String[]{permission}, mPermissionRequestCode);
        }
    }

    @Override
    public void showMessage(String title, String message) {
        // ignore show dialog if activity stopped
        if (isFinishing()) {
            return;
        }

        showDialogMessage(title, message);
    }

    /**
     * show network
     */
    public void showDialogNetwork() {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle("error")
                    .setMessage("error")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    /**
     * show message and title
     *
     * @param title
     * @param message
     */
    public void showDialogMessage(String title, String message) {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    /**
     * show option dialog
     */
    protected void showMessageOptionDialog(String message, String stringYes, String stringNo, final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(stringYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onClick(dialog, id);
                        }
                    }
                })
                .setNegativeButton(stringNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * Method default of activity.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mPermissionRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionsCallBack.onGrant(permissions[0]);
            } else {
                mPermissionsCallBack.onDeny(false);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
