package com.hades.permission;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.Map;

/**
 * SimplePermissions is a tool to request android runtime permission
 */
public class PermissionsTool {
    public static final String TAG = "SimplePermissions";
    private FragmentActivity activity;

    private static final String EX_PERMISSION_NOT_VALID = "No permissions to request";
    private static final String EX_ACTIVITY_NULL = "activity is null";

    private ActivityResultLauncher<String[]> mResultLauncher;

    @MainThread
    public PermissionsTool(FragmentActivity activity) throws NullPointerException {
        this.activity = activity;
    }

    /**
     * When request unrequested permissions
     *
     * @param callback    the callback for requesting unrequested permissions
     * @param permissions the request unrequested permissions
     */
    public void request(final @NonNull String[] permissions, @NonNull OnResultCallback callback) {
        if (permissions.length == 0) {
            callback.onError(EX_PERMISSION_NOT_VALID);
            Log.e(TAG, "request: " + EX_PERMISSION_NOT_VALID);
            return;
        }
        if (null == activity) {
            callback.onError(EX_ACTIVITY_NULL);
            Log.e(TAG, "request: " + EX_ACTIVITY_NULL);
            return;
        }
        try {
            if (isGranted(permissions)) {
                callback.granted();
                return;
            }
            if (shouldShowRationale(permissions)) {
                callback.showRationale(() -> requestPermissions(callback, permissions));
                return;
            }
            requestPermissions(callback, permissions);
        } catch (Exception ex) {
            Log.e(TAG, "request permission " + Arrays.toString(permissions) + "occurred error", ex);
            callback.onError("request permission " + Arrays.toString(permissions) + "occurred error:" + ex.getMessage());
        }
    }

    /**
     * Clear the resource
     */
    public void clear() {
        activity = null;
    }

    private void requestPermissions(@NonNull OnResultCallback callback, final @NonNull String[] permissions) {
        mResultLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> permissionsResult) {
                boolean result = isAllGranted(permissionsResult);
                if (result) {
                    callback.granted();
                } else {
                    callback.denied();
                }
                mResultLauncher.unregister();
                mResultLauncher = null;
            }
        });
        mResultLauncher.launch(permissions);
    }

    private boolean isGranted(final String[] permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean isGranted(final String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission);
    }

    private boolean shouldShowRationale(final @NonNull String[] permissions) {
        for (String p : permissions) {
            if (shouldShowRationale(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldShowRationale(final String permission) {
        return !isGranted(permission) && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    private boolean isAllGranted(Map<String, Boolean> permissionsResult) {
        if (null == permissionsResult || permissionsResult.isEmpty()) {
            return false;
        }
        for (Boolean value : permissionsResult.values()) {
            if (Boolean.FALSE.equals(value)) {
                return false;
            }
        }
        return true;
    }
}