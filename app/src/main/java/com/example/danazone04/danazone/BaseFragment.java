package com.example.danazone04.danazone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.lang.ref.WeakReference;

/**
 * Created by PC on 1/17/2018.
 */

@EFragment
public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();

    private WeakReference<Activity> mWeakReference;
    private ProgressDialog mProgressDialog;

    @AfterViews
    protected abstract void afterViews();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeakReference = new WeakReference<Activity>(getActivity());
        intProgressBar();
    }

    public Activity getBaseActivity() {
        return mWeakReference.get();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            getView().setClickable(true);
        }
    }

    protected void showAlertDialog(@NonNull String msg, final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(getBaseActivity())
                .setMessage(msg)
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

    private void intProgressBar() {
        mProgressDialog = new ProgressDialog(getBaseActivity());
        //mProgressDialog.setMessage(getString(R.string.message_waiting));
        mProgressDialog.setCancelable(false);
    }

    /**
     * Show dialog inside fragment.
     */
    public void showProgressDialog() {
        if (getBaseActivity() != null && !getBaseActivity().isFinishing() && isAdded() && mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Off dialog inside fragment.
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

    }

    /**
     * show network
     */
    public void showDialogNetwork() {
        if (!getBaseActivity().isFinishing()) {
            new AlertDialog.Builder(getBaseActivity())
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
     * show option dialog
     */
    protected void showMessageOptionDialog(String message, final DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(getBaseActivity())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onClick(dialog, id);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    protected void showAlertDialog(String message) {
        showAlertDialog(message, null);
    }
}
