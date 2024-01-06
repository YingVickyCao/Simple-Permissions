# SimplePermissions

[![](https://jitpack.io/v/yingvickycao/simplepermissions.svg)](https://jitpack.io/#yingvickycao/simplepermissions)


This library allows to request runtime permission.

# 1 Setup

To use this library your `minSdkVersion` must be >= Android 8 (API 26).

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.yingvickycao:simplepermissions:0.2'
}
```

Ref : https://jitpack.io/#yingvickycao/simplepermissions/v0.2

# 2 Usage

Example : if want to request `Manifest.permission.RECORD_AUDIO`

```xml
<!-- First, Add RECORD_AUDIO permission to your AndroidManifest.xml -->
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
```

```android
 private void requestRuntimePermission() {
        PermissionsTool permissionsTool = new PermissionsTool(this);
        permissionsTool.request(new String[]{Manifest.permission.RECORD_AUDIO}, new OnResultCallback() {
            @Override
            public void showRationale(OnRationaleClickListener listener) {
                Log.d(PermissionsTool.TAG, "showRationaleContextUI: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Request permission").setMessage("Permission Audio / SD")
                        .setPositiveButton(getString(R.string.ok), (dialog, which) -> listener.onClick())
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                        })
                        .setNeutralButton(getString(R.string.skip), (dialog, which) -> {
                        }).create().show();
            }

            @Override
            public void granted() {
                Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void denied() {
                Toast.makeText(MainActivity.this, "Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: " + message);
            }
        });
        permissionsTool.clear();
```

# [Releases](./doc/Releases.md)

# [FAQ](./doc/FAQ.md)

# License

Copyright is ` Apache License`, see [LICENSE](./LICENSE),
and visit website http://www.apache.org/licenses/LICENSE-2.0
