package ying.cao.simplepermissions;


public interface IRequestPermissionsCallback extends IPermissionsResult {
    default void showRationaleContextUI(IRationaleOnClickListener callback) {

    }

    default void cancel() {

    }
}
