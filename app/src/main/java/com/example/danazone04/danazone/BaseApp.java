package com.example.danazone04.danazone;

import android.annotation.SuppressLint;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.androidannotations.annotations.EApplication;

/**
 * Created by PC on 1/17/2018.
 */

@SuppressLint("Registered")
@EApplication
public class BaseApp extends MultiDexApplication {
    private static final String TAG = BaseApp.class.getSimpleName();
    private static BaseApp sInstance = null;
    private boolean mIsInForegroundMode;

    /**
     * Get instance of app
     *
     * @return app
     */
    public static synchronized BaseApp getInstance() {
        if (sInstance == null) {
            sInstance = new BaseApp();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // Session Manager SharedPreferences
        SessionManager.getInstance().init(this);

        MultiDex.install(getApplicationContext());

    }

    public void activityResumed() {
        mIsInForegroundMode = true;
    }

    public void activityPaused() {
        mIsInForegroundMode = false;
    }

    public boolean isInForeground() {
        return mIsInForegroundMode;
    }
}

