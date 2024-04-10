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
     * Runtime permission is granted.
     * This function invoked if user has granted the permission before, or user chooses "allow" on the system permission dialog
     */
    void onPermissionGranted();

    /**
     * Runtime permission us denied.
     * This function invoked if user chooses "cancel" on rationale UI, or user chooses "Not allow" on system permission dialog.
     */
    void onPermissionDenied();

    /**
     * Error occurred on runtime permission request.
     *
     * @param message error message
     */
    void onPermissionRequestError(String message);
}
