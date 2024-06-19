package com.hades.utility.permission;

public interface OnPermissionResultCallback extends OnSimplePermissionCallback {
    /**
     * ¡
     * In an educational UI, explain to the user why your app requires this
     * permission for a specific feature to behave as expected, and what features are disabled if it's declined.
     * In this UI, include a "cancel" or "no thanks" button that lets the user continue
     * using your app without granting the permission.
     *
     * @param callback Rationale UI listener
     */
    void showInContextUI(OnContextUIListener callback);
}
