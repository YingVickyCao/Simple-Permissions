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
    private boolean mLogging;

    ActivityResultLauncher<String[]> mResultLauncher;
    IPermissionsResult mCallback;

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    public void setCallback(IPermissionsResult callback) {
        this.mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mResultLauncher = requestPermissions();
        super.onCreate(savedInstanceState);
    }

    public ActivityResultLauncher<String[]> getResultLauncher() {
        return mResultLauncher;
    }

    private ActivityResultLauncher<String[]> requestPermissions() {
        return registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> permissionsResult) {
                if (isAllGranted(permissionsResult)) {
                    mCallback.granted();
                } else {
                    mCallback.denied();
                }
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

    public void requestMultiplePermissions(String... unrequestedPermissions) {
        if (!isPermissionsNotEmpty(unrequestedPermissions)) {
            mCallback.error(new Exception("Not permission to request"), unrequestedPermissions);
            return;
        }
        mResultLauncher.launch(unrequestedPermissions);
    }

    private boolean isPermissionsNotEmpty(String[] unrequestedPermissions) {
        return null != unrequestedPermissions && unrequestedPermissions.length > 0;
    }

    public void log(String message) {
        if (mLogging) {
            Log.d(SimplePermissions.TAG, message);
        }
    }
}
