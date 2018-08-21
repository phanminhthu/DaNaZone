package com.example.danazone04.danazone;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Created by PC on 1/17/2018.
 */

public class SessionManager {
    private static final String SHARED_PREFERENCES_NAME = "com.example.danazone04.danazone";
    private static final String KEY_SAVE_IMAGE_START = "key_save_image_start";
    private static final String KEY_SAVE_IMAGE_END = "key_save_image_end";
    private static final String KEY_SAVE_ID = "key_save_id";
    private static final String KEY_SAVE_PASS = "key_save_pass";
    private static final String KEY_SAVE_COIN = "key_save_coin";
    private static final String KEY_SAVE_CODE = "key_save_code";

    private static SessionManager sInstance;

    private SharedPreferences sharedPref;

    public synchronized static SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    private SessionManager() {
        // no instance
    }

    public void init(Context context) {
        sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Set key save email
     *
     * @param token
     */
    public void setKeyImageStart(String token) {
        sharedPref.edit().putString(KEY_SAVE_IMAGE_START, token).apply();
    }

    /**
     * get key save email
     *
     * @return
     */
    public String getKeySaveImageStart() {
        return sharedPref.getString(KEY_SAVE_IMAGE_START, "");
    }

    /**
     * Set key save pass
     *
     * @param token
     */
    public void setKeySaveImageEnd(Bitmap token) {
        sharedPref.edit().putString(KEY_SAVE_IMAGE_END, String.valueOf(token)).apply();
    }

    /**
     * get key save pass
     *
     * @return
     */
    public String getKeySaveImageEnd() {
        return sharedPref.getString(KEY_SAVE_IMAGE_END, "");
    }


    /**
     * get key save pass
     *
     * @return
     */
    public String getKeySavePass() {
        return sharedPref.getString(KEY_SAVE_PASS, "");
    }

    public void setKeySavePass(String token) {
        sharedPref.edit().putString(KEY_SAVE_PASS, token).apply();
    }

    /**
     * get key save pass
     *
     * @return
     */
    public String getKeySaveId() {
        return sharedPref.getString(KEY_SAVE_ID, "");
    }
    public void setKeySaveId(String token) {
        sharedPref.edit().putString(KEY_SAVE_ID, token).apply();
    }

    public String getKeySaveCoin() {
        return sharedPref.getString(KEY_SAVE_COIN, "");
    }
    public void setKeySaveCoin(String token) {
        sharedPref.edit().putString(KEY_SAVE_COIN, token).apply();
    }

    public void updateCoin(String token){
        sharedPref.edit().putString(KEY_SAVE_COIN, token).apply();
    }

    public String getKeySaveCode() {
        return sharedPref.getString(KEY_SAVE_CODE, "");
    }
    public void setKeySaveCode(String token) {
        sharedPref.edit().putString(KEY_SAVE_CODE, token).apply();
    }
    public void updateCode(String token){
        sharedPref.edit().putString(KEY_SAVE_CODE, token).apply();
    }
//    /**
//     * remove key save pass
//     *
//     * @return
//     */
//    public void removeSavePass() {
//        sharedPref.edit().remove(KEY_SAVE_PASS).commit();
//    }
//
//    /**
//     * Set key save info
//     */
//    public void setKeyInfo(String info) {
//        sharedPref.edit().putString(KEY_SAVE_INFO, info).apply();
//    }
//
//    /**
//     * get key save pass
//     *
//     * @return
//     */
//    public String getKeySaveInfo() {
//        return sharedPref.getString(KEY_SAVE_INFO, "");
//    }
}

