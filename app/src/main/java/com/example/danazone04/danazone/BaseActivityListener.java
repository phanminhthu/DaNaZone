package com.example.danazone04.danazone;

/**
 * Created by PC on 1/17/2018.
 */

interface BaseActivityListener {
    void onRequestPermission(String permission, PermissionRequestCallback callback);

    void showMessage(String message);

    void showMessage(String title, String message);

    void showProgressDialog();

    void dismissProgressDialog();
}
