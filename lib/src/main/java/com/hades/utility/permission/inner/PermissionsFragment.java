package com.hades.utility.permission.inner;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

final class PermissionsFragment extends Fragment {
    private static final String TAG = "PermissionsFragment";
    private ActivityResultLauncher<String[]> mResultLauncher;
    private ActivityResultCallback<Map<String, Boolean>> mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> permissionsResult) {
                mCallback.onActivityResult(permissionsResult);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mResultLauncher.unregister();
        mResultLauncher = null;
    }

    void requestPermissions(final String[] unrequestedPermissions, @NonNull final ActivityResultCallback<Map<String, Boolean>> callback) {
        mCallback = callback;
        mResultLauncher.launch(unrequestedPermissions);
    }
}