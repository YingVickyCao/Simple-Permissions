package com.hades.utility.permission;

public interface OnResultCallback {
    /**
     * In an educational UI, explain to the user why your app requires this
     * permission for a specific feature to behave as expected, and what features are disabled if it's declined.
     * In this UI, include a "cancel" or "no thanks" button that lets the user continue
     * using your app without granting the permission.
     *
     * @param callback Rationale UI listener
     */
    void showInContextUI(OnContextUIListener callback);

    /**
     * Request runtime permission successfully.
     */
    void granted();

    /**
     * Deny on runtime permission request.
     */
    void denied();

    /**
     * Error occurred on runtime permission request.
     *
     * @param message error message
     */
    void onError(String message);
}
