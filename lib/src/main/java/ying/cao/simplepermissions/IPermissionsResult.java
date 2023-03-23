package ying.cao.simplepermissions;

import android.util.Log;

public interface IPermissionsResult {
    void granted();

    default void denied() {

    }

    default void error(Exception ex, String... permissions) {
        Log.d(SimplePermissions.TAG, permissions + ",error:" + ex);
    }
}
