# Release Logs

# 0.1

Release `SimplePermissions` which supports `AndroidX` to request runtime permission, sdk support `minSdk 26` and `targetSdk 33`.


# 0.2
To keep it simple, remove other method to request runtime permission, only keep `public void request(@Rationale int rationaleBehaviour, IPermissionCallback callback, String... permissions){} `