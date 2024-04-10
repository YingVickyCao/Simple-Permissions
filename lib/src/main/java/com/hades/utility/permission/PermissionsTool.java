package com.hades.utility.permission;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hades.utility.permission.inner.PermissionsToolImpl;

// https://developer.android.google.cn/training/permissions/requesting

/**
 * SimplePermissions is a tool to request android runtime permission
 */
public class PermissionsTool {
    private PermissionsToolImpl impl;

    @MainThread
    public PermissionsTool(FragmentActivity activity) {
        impl = new PermissionsToolImpl(activity);
    }

    @MainThread
    public PermissionsTool(Fragment fragment) {
        impl = new PermissionsToolImpl(fragment);
    }

    public void request(@NonNull String[] permissions, @NonNull OnResultCallback callback) {
        impl.request(permissions, callback);
    }
}