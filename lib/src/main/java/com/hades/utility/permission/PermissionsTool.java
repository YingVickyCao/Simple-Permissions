package com.hades.utility.permission;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Arrays;
import java.util.Map;

// https://developer.android.google.cn/training/permissions/requesting

/**
 * SimplePermissions is a tool to request android runtime permission
 */
public class PermissionsTool {
    public static final String TAG = "SimplePermissions";

    private static final String EX_PERMISSION_NOT_VALID = "No permissions to request";
    private static final String EX_ACTIVITY_NULL = "activity is null";

    private final PermissionsFragment mPermissionsFragment;

    @MainThread
    public PermissionsTool(FragmentActivity activity) {
        mPermissionsFragment = getPermissionsFragment(activity.getSupportFragmentManager());
    }

    @MainThread
    public PermissionsTool(Fragment fragment) {
        mPermissionsFragment = getPermissionsFragment(fragment.getChildFragmentManager());
    }

    private PermissionsFragment getPermissionsFragment(@NonNull FragmentManager fragmentManager) {
        PermissionsFragment fragment = (PermissionsFragment) fragmentManager.findFragmentByTag(TAG);
        boolean isNewInstance = (fragment == null);
        if (!isNewInstance) {
            return fragment;
        }
        fragment = new PermissionsFragment();
        fragmentManager.beginTransaction().add(fragment, TAG).commitNow();
        return fragment;
    }

    /**
     * Request runtime permissions
     *
     * @param callback    the callback for requesting unrequested permissions
     * @param permissions the request unrequested permissions
     */
    public void request(final @NonNull String[] permissions, @NonNull OnResultCallback callback) {
        if (permissions.length == 0) {
            callback.onPermissionRequestError(EX_PERMISSION_NOT_VALID);
            Log.e(TAG, "request: " + EX_PERMISSION_NOT_VALID);
            return;
        }
        if (null == mPermissionsFragment.getActivity()) {
            callback.onPermissionRequestError(EX_ACTIVITY_NULL);
            Log.e(TAG, "request: " + EX_ACTIVITY_NULL);
            return;
        }
        try {
            if (isPermissionsAllGranted(permissions)) {
                callback.onPermissionGranted();
                return;
            } else if (isShouldShowRationale(permissions)) {
                callback.showInContextUI(new OnContextUIListener() {
                    @Override
                    public void ok() {
                        requestPermissions(callback, permissions);
                    }

                    @Override
                    public void cancel() {
                        callback.onPermissionDenied();
                    }
                });
                return;
            }
            requestPermissions(callback, permissions);
        } catch (Exception ex) {
            Log.e(TAG, "request permission " + Arrays.toString(permissions) + "occurred error", ex);
            callback.onPermissionRequestError("request permission " + Arrays.toString(permissions) + "occurred error:" + ex.getMessage());
        }
    }

    private void requestPermissions(@NonNull OnResultCallback callback, final @NonNull String[] permissions) {
        mPermissionsFragment.requestPermissions(permissions, new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (isAllGranted(result)) {
                    callback.onPermissionGranted();
                } else {
                    callback.onPermissionDenied();
                }
            }
        });
    }

    private boolean isPermissionsAllGranted(final String[] permissions) {
        for (String permission : permissions) {
            if (!isPermissionAllGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPermissionAllGranted(final String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mPermissionsFragment.getActivity(), permission);
    }

    private boolean isShouldShowRationale(final @NonNull String[] permissions) {
        for (String p : permissions) {
            if (isShouldShowRationale(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean isShouldShowRationale(final String permission) {
        return !isPermissionAllGranted(permission) && ActivityCompat.shouldShowRequestPermissionRationale(mPermissionsFragment.getActivity(), permission);
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