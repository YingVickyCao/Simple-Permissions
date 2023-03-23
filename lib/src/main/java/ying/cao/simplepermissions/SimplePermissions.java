package ying.cao.simplepermissions;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class SimplePermissions implements IPermissionTools {
    public static final String TAG = "SimplePermissions";

    private final FragmentActivity mActivity;
    private final PermissionsFragment mPermissionsFragment;

    public SimplePermissions(FragmentActivity activity) {
        mActivity = activity;
        mPermissionsFragment = getRxPermissionsFragment(activity);
    }

    @Override
    public void request(IRequestPermissionsCallback callback, String... permissions) {
        request(false, callback, permissions);
    }

    @Override
    public void request4AlwaysRationale(IRequestPermissionsCallback callback, String... permissions) {
        request(true, callback, permissions);
    }

    @Override
    public void request4IgnoreRationale(IPermissionsResult callback, String... permissions) {
        try {
            if (isGranted(permissions)) {
                callback.granted();
                return;
            }
            requestMultiplePermissions(callback, permissions);
        } catch (Exception ex) {
            callback.error(ex, permissions);
        }
    }

    @Override
    public boolean isGranted(final String... permissions) {
        if (permissions == null) {
            throw new RuntimeException("permissions is null");
        }
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(final String... permissions) {
        for (String p : permissions) {
            if (shouldShowPermissionRequestPermissionRationale(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void request(IPermissionsResult callback, final String... permissions) {
        try {
            requestMultiplePermissions(callback, permissions);
        } catch (Exception ex) {
            callback.error(ex, permissions);
        }
    }

    private PermissionsFragment getRxPermissionsFragment(FragmentActivity activity) {
        PermissionsFragment permissionsFragment = findPermissionsFragment(activity);
        boolean isNewInstance = (permissionsFragment == null);
        if (isNewInstance) {
            permissionsFragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(permissionsFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permissionsFragment;
    }

    private PermissionsFragment findPermissionsFragment(FragmentActivity activity) {
        return (PermissionsFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    private void request(boolean isAlwaysRationale, IRequestPermissionsCallback callback, final String... permissions) {
        try {
            if (isGranted(permissions)) {
                callback.granted();
                return;
            }
            if (isWillShowRationale(isAlwaysRationale, permissions)) {
                callback.showRationaleContextUI(new IRationaleOnClickListener() {
                    @Override
                    public void clickOK() {
                        requestMultiplePermissions(callback, permissions);
                    }

                    @Override
                    public void clickCancel() {
                        callback.cancel();
                    }

                    @Override
                    public void clickSkip() {
                        callback.cancel();
                    }
                });
                return;
            }
            requestMultiplePermissions(callback, permissions);
        } catch (Exception ex) {
            callback.error(ex, permissions);
        }
    }

    private boolean isWillShowRationale(boolean isAlwaysRationale, final String... permissions) {
        return isAlwaysRationale || shouldShowRequestPermissionRationale(permissions);
    }

    private void requestMultiplePermissions(IPermissionsResult callback, String... unrequestedPermissions) {
        mPermissionsFragment.setCallback(callback);
        mPermissionsFragment.requestMultiplePermissions(unrequestedPermissions);
    }

    private boolean isGranted(final String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mActivity, permission);
    }

    private boolean isRevoked(String permission) {
        return mActivity.getPackageManager().isPermissionRevokedByPolicy(permission, mActivity.getPackageName());
    }

    private boolean shouldShowPermissionRequestPermissionRationale(final String permission) {
        return !isGranted() && ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
    }
}