package com.example.danazone04.danazone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PC on 1/17/2018.
 */

public class SessionManager {
    private static final String SHARED_PREFERENCES_NAME = "com.ulatech.linkclick";
    private static final String KEY_SAVE_PHONE = "key_save_phone_user";
    private static final String KEY_SAVE_ID = "key_save_id_user";

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
    public void setKeySavePhone(String token) {
        sharedPref.edit().putString(KEY_SAVE_PHONE, token).apply();
    }

    /**
     * get key save email
     *
     * @return
     */
    public String getKeySavePhone() {
        return sharedPref.getString(KEY_SAVE_PHONE, "");
    }

    /**
     * Set key save pass
     *
     * @param token
     */
    public void setKeySaveID(String token) {
        sharedPref.edit().putString(KEY_SAVE_ID, token).apply();
    }

    /**
     * get key save pass
     *
     * @return
     */
    public String getKeySaveID() {
        return sharedPref.getString(KEY_SAVE_ID, "");
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

