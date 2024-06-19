package com.hades.utility.permission;

public interface OnSimplePermissionCallback {
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
    void onPermissionError(String message);
}
