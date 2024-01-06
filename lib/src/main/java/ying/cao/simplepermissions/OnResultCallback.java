package ying.cao.simplepermissions;

public interface OnResultCallback {
    void showRationale(OnRationaleClickListener callback);

    void granted();

    void denied();

    void onError(String message);
}
