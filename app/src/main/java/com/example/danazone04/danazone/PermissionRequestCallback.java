package com.example.danazone04.danazone;

/**
 * Created by PC on 1/17/2018.
 */

public interface PermissionRequestCallback {
    void onGrant(String permission);

    void onDeny(boolean needShowExplain);
}
