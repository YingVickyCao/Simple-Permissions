package ying.cao.simplepermissions;

public interface IPermissionTools {
    void request(IRequestPermissionsCallback callback, final String... permissions);

    void request4AlwaysRationale(IRequestPermissionsCallback callback, final String... permissions);

    void request4IgnoreRationale(IPermissionsResult callback, final String... permissions);

    public boolean isGranted(final String... permissions);

    boolean shouldShowRequestPermissionRationale(final String... permissions);

    void request(IPermissionsResult callback, final String... permissions);

}
