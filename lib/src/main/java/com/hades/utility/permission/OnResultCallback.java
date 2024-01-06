package com.hades.permission;

public interface OnResultCallback {
    void showRationale(OnRationaleClickListener callback);

    void granted();

    void denied();

    void onError(String message);
}
