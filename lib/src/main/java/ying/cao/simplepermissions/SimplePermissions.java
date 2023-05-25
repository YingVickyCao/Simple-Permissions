package ying.cao.simplepermissions;

import android.content.pm.PackageManager;

import androidx.annotation.IntDef;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class SimplePermissions {
    public static final String TAG = "SimplePermissions";
    private final PermissionsFragment mPermissionsFragment;

    private static final String PERMISSION_NOT_VALID = "No permissions to request";

    public interface OnRationaleClickListener {
        /**
         * Called when click ok on Rationale UI
         */
        void onClick();
    }

    public interface IPermissionCallback {
        default void showRationaleContextUI(OnRationaleClickListener callback) {
        }

        void granted();

        default void denied() {
        }
    }


    /**
     * When request unrequested permissions, never show the rational context UI to user because the permission is not important.
     */
    public static final int NEVER = 0x00000004;
    /**
     * When request unrequested permissions, show the rational context UI to user if should.
     * This is the default and also recommended behaviour.
     */
    public static final int SHOULD = 0x00000000;
    /**
     * When request unrequested permissions, always show the rational context UI to user because you think the permission is very important, you want to explain each time.
     */
    public static final int ALWAYS = 0x00000008;

    @IntDef({SHOULD, NEVER, ALWAYS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Rationale {
    }

    interface RequestPermissionResultCallback {
        void onResult(boolean isGranted);
    }

    @MainThread
    public SimplePermissions(FragmentActivity activity) {
        mPermissionsFragment = getPermissionsFragment(activity.getSupportFragmentManager());
    }

    @MainThread
    public SimplePermissions(Fragment fragment) {
        mPermissionsFragment = getPermissionsFragment(fragment.getChildFragmentManager());
    }

    /**
     * When request unrequested permissions,according to your rationaleBehaviour value (one of  {@link SimplePermissions#SHOULD}, {@link SimplePermissions#NEVER}, or {@link SimplePermissions#ALWAYS}  rationale behaviour), shows rationale or not.
     *
     * @param rationaleBehaviour one of  {@link SimplePermissions#SHOULD}, {@link SimplePermissions#NEVER}, or {@link SimplePermissions#ALWAYS}  rationale behaviour
     * @param callback           the callback for requesting unrequested permissions
     * @param permissions        the request unrequested permissions
     */
    public void request(@Rationale int rationaleBehaviour, IPermissionCallback callback, String... permissions) {
        if (mPermissionsFragment.isPermissionsEmpty(permissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        try {
            if (isGranted(permissions)) {
                callback.granted();
                return;
            }
            if (ALWAYS == rationaleBehaviour || (SHOULD == rationaleBehaviour && shouldShowRequestPermissionRationale(permissions))) {
                callback.showRationaleContextUI(() -> doRequestPermissions(callback, permissions));
                return;
            }
            doRequestPermissions(callback, permissions);
        } catch (Exception ex) {
            mPermissionsFragment.log(Arrays.toString(permissions) + ",ex:" + ex);
        }
    }

    /**
     * When request unrequested permissions,never show rational context UI (use {@link SimplePermissions#NEVER}}
     *
     * @param callback    the callback for requesting unrequested permissions
     * @param permissions the request unrequested permissions
     */

    private void doRequestPermissions(IPermissionCallback callback, String... permissions) {
        if (mPermissionsFragment.isPermissionsEmpty(permissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        mPermissionsFragment.requestPermissions(isGranted -> {
            if (isGranted) {
                callback.granted();
            } else {
                callback.denied();
            }
        }, permissions);
    }

    private boolean isGranted(final String... permissions) {
        if (mPermissionsFragment.isPermissionsEmpty(permissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
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

    private boolean isGranted(final String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mPermissionsFragment.requireActivity(), permission);
    }

    private boolean shouldShowRequestPermissionRationale(final String... permissions) {
        if (mPermissionsFragment.isPermissionsEmpty(permissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        for (String p : permissions) {
            if (shouldShowPermissionRequestPermissionRationale(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldShowPermissionRequestPermissionRationale(final String permission) {
        return !isGranted(permission) && ActivityCompat.shouldShowRequestPermissionRationale(mPermissionsFragment.requireActivity(), permission);
    }

    private class InvalidPermissions extends RuntimeException {
        public InvalidPermissions(String message) {
            super(message);
        }
    }
}