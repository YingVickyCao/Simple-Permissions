package ying.cao.simplepermissions;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

public class PermissionsFragment extends Fragment {
    private static final String TAG = "PermissionsFragment";
    private boolean mLogging = true;

    ActivityResultLauncher<String[]> mResultLauncher;
    RequestPermissionResultCallback mCallback;

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mResultLauncher = registerPermissionsResult();
        super.onCreate(savedInstanceState);
    }

    private ActivityResultLauncher<String[]> registerPermissionsResult() {
        return registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> permissionsResult) {
                mCallback.checkResult(isAllGranted(permissionsResult));
            }
        });
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

    public void requestPermissions(String... unrequestedPermissions) {
        mResultLauncher.launch(unrequestedPermissions);
    }

    public void requestPermissions(final RequestPermissionResultCallback callback, final String... unrequestedPermissions) {
        if (isPermissionsEmpty(unrequestedPermissions)) {
            log("No permission to request");
            return;
        }
        mCallback = callback;
        mResultLauncher.launch(unrequestedPermissions);
    }

    boolean isPermissionsEmpty(String[] unrequestedPermissions) {
        return null != unrequestedPermissions && unrequestedPermissions.length > 0;
    }

    public void log(String message) {
        if (mLogging) {
            Log.d(SimplePermissions.TAG, message);
        }
    }

    interface RequestPermissionResultCallback {
        void checkResult(boolean isGranted);
    }
}
