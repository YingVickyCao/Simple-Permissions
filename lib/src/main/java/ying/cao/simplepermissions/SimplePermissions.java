package ying.cao.simplepermissions;

import android.content.pm.PackageManager;

import androidx.annotation.IntDef;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class SimplePermissions {
    public static final String TAG = "SimplePermissions";

    private final FragmentActivity mActivity;
    private final PermissionsFragment mPermissionsFragment;

    private final String PERMISSION_NOT_VALID = "No permissions to request";

    @IntDef({SHOULD, NEVER, ALWAYS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Rationale {
    }

    /**
     * When request unrequested permissions, show the rational context UI to user if should.
     * This is the default and also recommended behaviour.
     */
    public static final int SHOULD = 0x00000000;

    /**
     * When request unrequested permissions, never show the rational context UI to user because the permission is not important.
     */
    public static final int NEVER = 0x00000004;

    /**
     * When request unrequested permissions, always show the rational context UI to user because you think the permission is very important, you want to explain each time.
     */
    public static final int ALWAYS = 0x00000008;

    public interface IPermissionCallback {
        default void showRationaleContextUI(OnRationaleClickListener callback) {
        }

        void granted();

        default void denied() {
        }
    }

    public interface IPurePermissionCallback {
        void granted();

        default void denied() {
        }
    }

    public interface OnRationaleClickListener {
        /**
         * Called when click ok on Rationale UI
         */
        void onClick();
    }


    public SimplePermissions(FragmentActivity activity) {
        mActivity = activity;
        mPermissionsFragment = getRxPermissionsFragment(activity);
    }

    public void request(IPermissionCallback callback, String... permissions) {
        request(SHOULD, callback, permissions);
    }

    class InvalidPermissions extends RuntimeException {
        public InvalidPermissions(String message) {
            super(message);
        }
    }

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

    private void doRequestPermissions(IPermissionCallback callback, String... permissions) {
        request(new IPurePermissionCallback() {
            @Override
            public void granted() {
                callback.granted();
            }

            @Override
            public void denied() {
                callback.denied();
            }
        }, permissions);
    }

    public void request(IPurePermissionCallback callback, final String... permissions) {
        if (mPermissionsFragment.isPermissionsEmpty(permissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        try {
            requestPermissions(callback, permissions);
        } catch (Exception ex) {
            mPermissionsFragment.log(Arrays.toString(permissions) + ",ex:" + ex);
        }
    }

    public boolean isGranted(final String... permissions) {
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

    private PermissionsFragment getRxPermissionsFragment(FragmentActivity activity) {
        PermissionsFragment fragment = findPermissionsFragment(activity);
        boolean isNewInstance = (fragment == null);
        if (isNewInstance) {
            fragment = new PermissionsFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().add(fragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private PermissionsFragment findPermissionsFragment(FragmentActivity activity) {
        return (PermissionsFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    private void requestPermissions(IPurePermissionCallback callback, String... unrequestedPermissions) {
        if (mPermissionsFragment.isPermissionsEmpty(unrequestedPermissions)) {
            throw new InvalidPermissions(PERMISSION_NOT_VALID);
        }
        mPermissionsFragment.requestPermissions(isGranted -> {
            if (isGranted) {
                callback.granted();
            } else {
                callback.denied();
            }
        }, unrequestedPermissions);
    }

    private boolean isGranted(final String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mActivity, permission);
    }

    public boolean shouldShowRequestPermissionRationale(final String... permissions) {
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
        return !isGranted() && ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
    }
}